package com.tvdgapp.services.reference.init;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvdgapp.constants.Constants;
import com.tvdgapp.constants.SchemaConstant;
import com.tvdgapp.exceptions.ServiceException;
import com.tvdgapp.exceptions.TvdgException;
import com.tvdgapp.models.reference.countrycode.LocaleCountry;
import com.tvdgapp.models.reference.countrycode.LocaleCountryDto;
import com.tvdgapp.models.reference.countrycode.LocaleState;
import com.tvdgapp.models.reference.countrycode.LocaleStateDto;
import com.tvdgapp.models.user.*;
import com.tvdgapp.models.user.admin.AdminUser;
import com.tvdgapp.models.user.admin.GenerateCode;
import com.tvdgapp.services.reference.country.CountryService;
import com.tvdgapp.services.reference.states.StatesService;
import com.tvdgapp.services.system.SystemConfigurationService;
import com.tvdgapp.services.user.PermissionService;
import com.tvdgapp.services.user.RoleService;
import com.tvdgapp.services.user.admin.AdminUserService;
import com.tvdgapp.utils.SecurityRolesBuilder;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service("initializationDatabase")
@Slf4j
public class InitializationDatabaseImpl implements InitializationDatabase {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializationDatabaseImpl.class);

    private final RoleService roleService;
    private final PermissionService permissionService;
    private final SystemConfigurationService systemConfigurationService;
    private final StatesService statesService;
    private final CountryService countryService;
    private final AdminUserService adminUserService;
    private final PasswordEncoder passwordEncoder;


    private @Nullable String name;

    public boolean isEmpty() {
        return roleService.count() == 0;
    }

    @Transactional
    public void populate(String contextName) {
        this.name = contextName;
        createRoles();
        createDefaultAdmin();
//        createState();
        createLocaleStates();
        createCountries();
//        createLocaleCity();
        createConfigurations();
    }





    private void createDefaultAdmin() throws ServiceException {
        if (this.adminUserService.findUserByEmail(SchemaConstant.DEFAULT_SUPER_ADMINISTRATOR_EMAIL).isEmpty()) {
            AdminUser user = new AdminUser();
            String password = passwordEncoder.encode(SchemaConstant.DEFAULT_SUPER_ADMINISTRATOR_DEFAULT_PASS);
            Role role = roleService.fetchByRoleKey(Constants.ROLE_SUPER_ADMIN).orElseThrow(() -> new TvdgException.EntityNotFoundException("Resource not found for role with key:" + Constants.ROLE_SUPER_ADMIN));
            //creation of the super admin admin:password)
            user.setFirstName("Administrator");
            user.setLastName("SuperAdmin");
            user.setEmail(SchemaConstant.DEFAULT_SUPER_ADMINISTRATOR_EMAIL);
            user.setPassword(password);
            user.setUserType(UserType.valueOf(UserType.ADMIN.name()));
            user.addRole(role);
//            user.setGenerateCode(GenerateCode.NO);
            user.setStatus(UserStatus.ACTIVE);
            adminUserService.create(user);
        }
    }

    private void createRoles() {

        LOGGER.info(String.format("%s : Creating Security groups ", name));

        Collection<Permission> definedPermissions = this.permissionService.fetchPermissions();
        Map<String, Permission> definedPermsMap = this.mapByPermission(definedPermissions);

        // Get system permissions from JSON file
        Collection<Permission> systemPermissions = this.loadSystemPermissions("reference/permissions.json");

        for (Permission permission : systemPermissions) {
            if (!definedPermsMap.containsKey(permission.getPermission())) {
                definedPermsMap.put(permission.getPermission(), this.createPermission(permission));
            }
        }

        SecurityRolesBuilder roleBuilder = new SecurityRolesBuilder();
        Optional<Role> optionalRole;

        // Super Admin role
        optionalRole = roleService.fetchByRoleKey(Constants.ROLE_SUPER_ADMIN);
        Role superAdminRole = optionalRole.orElseGet(() -> roleBuilder.addRole("SUPER ADMINISTRATOR", Constants.ROLE_SUPER_ADMIN, RoleType.ADMIN).getLastRole());

        // Assign all permissions to Super Admin
        for (Permission permission : definedPermsMap.values()) {
            superAdminRole.addPermission(permission);
        }

// Admin Member role
//        optionalRole = roleService.fetchByRoleKey(Constants.ROLE_ADMIN_MEMBER);
//        Role adminMemberRole = optionalRole.orElseGet(() -> roleBuilder.addRole("ADMIN MEMBER", Constants.ROLE_ADMIN_MEMBER, RoleType.ADMIN).getLastRole());
//        assignPermissions(adminMemberRole, definedPermsMap, "adminMemberAccess");

        // Admin role
        optionalRole = roleService.fetchByRoleKey(Constants.ROLE_ADMIN);
        Role adminRole = optionalRole.orElseGet(() -> roleBuilder.addRole("ADMIN", Constants.ROLE_ADMIN, RoleType.ADMIN).getLastRole());
        assignPermissions(adminRole, definedPermsMap, "adminAccess");

        // Finance role
        optionalRole = roleService.fetchByRoleKey(Constants.ROLE_FINANCE);
        Role financeRole = optionalRole.orElseGet(() -> roleBuilder.addRole("FINANCE", Constants.ROLE_FINANCE, RoleType.ADMIN).getLastRole());
        assignPermissions(financeRole, definedPermsMap, "financeAdminAccess");

        // Customer Service Executive role
        optionalRole = roleService.fetchByRoleKey(Constants.ROLE_CUSTOMER_SERVICE_EXECUTIVE);
        Role customerServiceRole = optionalRole.orElseGet(() -> roleBuilder.addRole("CUSTOMER SERVICE EXECUTIVE", Constants.ROLE_CUSTOMER_SERVICE_EXECUTIVE, RoleType.ADMIN).getLastRole());
        assignPermissions(customerServiceRole, definedPermsMap, "customerServiceExecutiveAdminAccess");

        // Customer role
        optionalRole = roleService.fetchByRoleKey(Constants.ROLE_CUSTOMER);
        Role customerRole = optionalRole.orElseGet(() -> roleBuilder.addRole("CUSTOMER", Constants.ROLE_CUSTOMER, RoleType.CUSTOMER).getLastRole());
        assignPermissions(customerRole, definedPermsMap, "customerUserAccess");

        // Affiliate role
        optionalRole = roleService.fetchByRoleKey(Constants.ROLE_AFFILIATE);
        Role affiliateRole = optionalRole.orElseGet(() -> roleBuilder.addRole("AFFILIATE", Constants.ROLE_AFFILIATE, RoleType.AFFILIATE).getLastRole());
        assignPermissions(affiliateRole, definedPermsMap, "affiliateUserAccess");

        // Rider role
        optionalRole = roleService.fetchByRoleKey(Constants.ROLE_RIDER);
        Role riderRole = optionalRole.orElseGet(() -> roleBuilder.addRole("RIDER", Constants.ROLE_RIDER, RoleType.RIDER).getLastRole());
        assignPermissions(riderRole, definedPermsMap, "riderUserAccess");


        // Adding roles to the role service
        for (Role role : roleBuilder.build()) {
            roleService.create(role);
        }

    }

    private void assignPermissions(Role role, Map<String, Permission> definedPermsMap, String permissionKey) {
        if (definedPermsMap.containsKey(permissionKey)) {
            role.addPermission(definedPermsMap.get(permissionKey));
        }
    }

    private Collection<Permission> loadSystemPermissions(String jsonFilePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(jsonFilePath);
            Collection<Permission> systemPermissions = mapper.readValue(in, new TypeReference<>() {
            });
            return systemPermissions;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }



//    private Collection<StateDto> loadLocationState(String jsonFilePath) {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            InputStream in = this.getClass().getClassLoader().getResourceAsStream(jsonFilePath);
//            return mapper.readValue(in, new TypeReference<>() {
//            });
//        } catch (Exception e) {
//            throw new ServiceException(e);
//        }
//    }
    private Collection<LocaleStateDto> loadLocationLocaleStates(String jsonFilePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(jsonFilePath);
            return mapper.readValue(in, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
    private Collection<LocaleCountryDto> loadLocationLocaleCountry(String jsonFilePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(jsonFilePath);
            return mapper.readValue(in, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
//    private Collection<LocaleCityDto> loadLocationLocaleCity(String jsonFilePath) {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            InputStream in = this.getClass().getClassLoader().getResourceAsStream(jsonFilePath);
//            return mapper.readValue(in, new TypeReference<>() {
//            });
//        } catch (Exception e) {
//            throw new ServiceException(e);
//        }
//    }


    /**
     * map collection of permission entities by permission as key and permission entity as value
     *
     * @param // permissions
     * @return
     */
    private Map<String, Permission> mapByPermission(Collection<Permission> permissions) {
        Map<String, Permission> permissionMap = new HashMap<>();
        permissions.forEach(permission -> permissionMap.put(permission.getPermission(), permission));
        return permissionMap;
    }


    private Permission createPermission(Permission perm) {
        permissionService.create(perm);
        return perm;
    }

//    private void createState() {
//        if (stateService.count() <= 0) {
//            LOGGER.info(" Populating State ");
//            Collection<StateDto> states = this.loadLocationState("reference/state.json");
//            for (StateDto stateDto : states) {
//                try {
//                    State state = new State();
//                    state.setCode(Objects.requireNonNull(stateDto.getCode()));
//                    state.setName(Objects.requireNonNull(stateDto.getName()));
//                    state.setSupported(stateDto.isSupported());
//                    stateService.create(state);
//                }catch(Exception e){
//                    log.error("cannot create state",e);
//                }
//            }
//        }
//    }
    private void createLocaleStates() {
        if (statesService.count() <= 0) {
            LOGGER.info(" Populating States ");
            Collection<LocaleStateDto> states = this.loadLocationLocaleStates("reference/locale_states.json");
            for (LocaleStateDto stateDto : states) {
                try {
                    LocaleState state = new LocaleState();
                    state.setId(stateDto.getId());
                    state.setStateName(stateDto.getState_name());
                    state.setCapitalName(stateDto.getCapital_name());
                    state.setCountryId(stateDto.getCountry_id());
                    state.setCountryCode(stateDto.getCountry_code());
                    state.setFipsCode(stateDto.getFips_code());
                    state.setIso2(stateDto.getIso2());
                    state.setCreatedAt(stateDto.getCreated_at());
                    state.setUpdatedAt(stateDto.getUpdated_at());
                    state.setFlag(stateDto.getFlag());
                    state.setWikiDataId(stateDto.getWikiDataId());
//                    state.setCreatedBy(stateDto.getCreatedBy());
//                    state.setDeleted(stateDto.getDeleted());
//                    state.setModifiedBy(stateDto.getModifiedBy());

                    // Validate required fields before saving
                    if (state.getStateName() == null || state.getCountryCode() == null ||
                            state.getFipsCode() == null || state.getIso2() == null || state.getFlag() == null) {
                        throw new IllegalArgumentException("Missing required fields for state: " + state.getStateName());
                    }
                    statesService.create(state);
                }catch(Exception e){
                    log.error("cannot create states",e);
                }
            }
        }
    }
    private void createCountries() {
        if (countryService.count() <= 0) {
            LOGGER.info("Populating Countries");
            Collection<LocaleCountryDto> countries = loadLocationLocaleCountry("reference/locale_countries.json");
            for (LocaleCountryDto countryDto : countries) {
                try {
                    LocaleCountry country = new LocaleCountry();
                    country.setId(countryDto.getId());
                    country.setCountryName(countryDto.getCountry_name());
                    country.setIso3(countryDto.getIso3());
                    country.setIso2(countryDto.getIso2());
                    country.setPhonecode(countryDto.getPhone_code());
                    country.setCapital(countryDto.getCapital());
                    country.setCurrency(countryDto.getCurrency());
                    country.setCreatedAt(countryDto.getCreated_at());
                    country.setUpdatedAt(countryDto.getUpdated_at());
                    country.setFlag(countryDto.getFlag());
                    country.setWikiDataId(countryDto.getWikiDataId());

                    // Validate required fields before saving
//                    if (country.getCountryName() == null || country.getIso3() == null || country.getIso2() == null ||
//                            country.getPhonecode() == null || country.getCapital() == null || country.getCurrency() == null) {
//                        throw new IllegalArgumentException("Missing required fields for country: " + countryDto.getCountry_name());
//                    }

                    countryService.create(country);
                } catch (Exception e) {
                    LOGGER.error("Failed to create country: " + countryDto.getCountry_name(), e);
                }
            }
        }
    }

//private void createLocaleCity() {
//        if (cityService.count() <= 0) {
//            LOGGER.info(" Populating Cities ");
//            Collection<LocaleCityDto> cities = this.loadLocationLocaleCity("reference/locale_cities2.json");
//            for (LocaleCityDto cityDto : cities) {
//                try {
//                    LocaleCity city = new LocaleCity();
//                    city.setId(cityDto.getId());
//                    city.setCountryCode(cityDto.getCountry_code());
//                    city.setFlag(cityDto.getFlag());
//                    city.setLatitude(String.valueOf(cityDto.getLatitude()));
//                    city.setLongitude(String.valueOf(cityDto.getLongitude()));
//                    city.setDateCreated(String.valueOf(cityDto.getDate_created()));
//                    city.setDateModified(String.valueOf(cityDto.getDate_modified()));
//                    city.setTsCreated(String.valueOf(cityDto.getTs_created()));
//                    city.setTsModified(String.valueOf(cityDto.getTs_modified()));
//                    city.setCreatedAt(String.valueOf(cityDto.getCreated_at()));
//                    city.setStateCode(cityDto.getState_code());
//                    city.setUpdatedOn(String.valueOf(cityDto.getUpdatedOn()));
//                    city.setWikiDataId(cityDto.getWikiDataId());
//                    city.setCityName(cityDto.getCity_name());
//                    city.setCreatedBy(cityDto.getCreated_by());
//                    city.setDeleted(String.valueOf(cityDto.getDeleted()));
//                    city.setModifiedBy(cityDto.getModified_by());
//
//
//                    // Validate required fields before saving
////                    if (city.getCityName() == null || city.getStateCode() == null || city.getCountryCode() == null ||
////                            city.getLatitude() == null || city.getLongitude() == null || city.getFlag() == null) {
////                        throw new IllegalArgumentException("Missing required fields for country: " + cityDto.getCity_name());
////                    }
//                    cityService.create(city);
//                }catch(Exception e){
//                    log.error("cannot create cities",e);
//                }
//            }
//        }
//    }


    private void createConfigurations() {

        LOGGER.info(String.format("%s : Creating default configurations ", name));
        this.systemConfigurationService.createDefaultConfigurations();
    }


}
