package com.tvdgapp.utils;

import com.tvdgapp.constants.Constants;
import com.tvdgapp.dtos.auth.LoginResponseDto;
import com.tvdgapp.dtos.auth.UserDto;
import com.tvdgapp.dtos.user.CustomerDtoResponse;
import com.tvdgapp.dtos.wallet.WalletDto;
import com.tvdgapp.models.user.Role;
import com.tvdgapp.models.user.User;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.models.wallet.Wallet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public class AuthUtils {

    public static Collection<String> buildAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public static LoginResponseDto createLoginResponse(User user, CustomerUser customerUser, Wallet wallet, String token) {

        System.out.println("User: " + user);
        System.out.println("CustomerUser: " + customerUser);
        System.out.println("Wallet: " + wallet);
        System.out.println("Token: " + token);

        UserDto userDto;
            userDto=new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getProfilePic(), user.getStatus().name(), user.getLastLogin(), user.getUserType());
        CustomerDtoResponse customerUserDto = null;
        if (customerUser != null) {
            String dateOfBirth = customerUser.getDateOfBirth() != null ? customerUser.getDateOfBirth().toString() : "null";
            Integer pricingCategory = customerUser.getPricingLevelId() != null ? customerUser.getPricingLevelId() : null;

            customerUserDto = new CustomerDtoResponse(
                    customerUser.getTitle(), customerUser.getCompanyName(), customerUser.getCompanyContactName(),
                    customerUser.getCompanyEmail(), customerUser.getCompanyPhoneNo(), customerUser.getCompanyRegNumber(), customerUser.getDesignation(), customerUser.getAddress(), customerUser.getCity(), customerUser.getState(),
                    customerUser.getPostalCode(), customerUser.getIndustry(), dateOfBirth, customerUser.getNatureOfBusiness(), customerUser.getCustomerType(), pricingCategory
            );
        }

        WalletDto walletDto = null;
        if (wallet != null) {
            walletDto = new WalletDto(wallet.getWalletId(), wallet.getUserId(), wallet.getBalance(), wallet.getWalletKey());
        }

        return new LoginResponseDto(userDto, customerUserDto,  walletDto, token);
    }

    public static LoginResponseDto createLoginResponse(User user, String token) {
        UserDto userDto;
            userDto=new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getProfilePic(), user.getStatus().name(), user.getLastLogin(), user.getUserType());
        return new LoginResponseDto(userDto, token);
    }
    public static LoginResponseDto createLoginResponse(User user) {
        UserDto userDto;
            userDto=new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getProfilePic(), user.getStatus().name(), user.getLastLogin(), user.getUserType());
        return new LoginResponseDto(userDto);
    }

    public static boolean isEmptyToken(String token) {
        return StringUtils.isEmpty(token);
    }

    public static  Collection<? extends GrantedAuthority> getUserAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private static Collection<String> getPrivileges(Collection<Role> roles) {
        Collection<String> privileges = new HashSet<>();
        roles.forEach((role) -> {
            privileges.add(Constants.ROLE_PREFIX +role.getRoleKey());
            role.getPermissions().forEach((perm) -> {
                privileges.add(perm.getPermission());
            });
        });
        return privileges;
    }

    private static List<GrantedAuthority> getGrantedAuthorities(Collection<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        privileges.forEach((privilege) -> {
            authorities.add(new SimpleGrantedAuthority(privilege));
        });
        return authorities;
    }

//    public static String encodeCredentials(String username, String password) {
//        String credentials = username + ":" + password;
//        return Base64.getEncoder().encodeToString(credentials.getBytes());
//    }

//    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        for (Role role : user.getRoles()) {
//        for (Permission permission : role.getPermissions()) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(permission.getPermission()));
//        }
//    }

}
