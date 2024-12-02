package com.tvdgapp.services.system;

import com.tvdgapp.constants.SchemaConstant;
import com.tvdgapp.dtos.system.ConfigurationMetadata;
import com.tvdgapp.dtos.system.SystemConfigurationDto;
import com.tvdgapp.models.SystemConfiguration;
import com.tvdgapp.repositories.system.SystemConfigurationRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import com.tvdgapp.services.system.mail.EmailConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


//@RequiredArgsConstructor
@Service
public class SystemConfigurationServiceImpl extends TvdgEntityServiceImpl<Long, SystemConfiguration> implements SystemConfigurationService {

    private static final String GENERAL = "general";
    private static final String EMAIL = "email";

    private final SystemConfigurationRepository repository;
    private final Environment env;

    @Autowired
    public SystemConfigurationServiceImpl(SystemConfigurationRepository repository, Environment env) {
        super(repository);
        this.repository = repository;
        this.env = env;
    }

    @Override
    public String findConfigValueByKey(String key) {
        return this.repository.findByConfigurationKey(key);
//        return optional.map(SystemConfiguration::getValue).orElse("");
    }

    @Override
    public void createDefaultConfigurations() {

        Collection<SystemConfiguration> systemConfigurations = this.repository.findAll();
        Map<String, SystemConfiguration> mapByKey = this.mapConfigurationsByKey(systemConfigurations);

        SystemConfiguration configuration = null;
        //company name
        configuration = mapByKey.get(SchemaConstant.COMPANY_NAME);
        if (configuration == null) {
            configuration = new SystemConfiguration();
        }
        configuration.setConfigurationKey(SchemaConstant.COMPANY_NAME);
        configuration.setConfigurationName("Company Name");
        configuration.setValue("Deluxe Link Express");
        configuration.setConfigurationGroup(GENERAL);
        configuration.setSortOrder(2);
        this.create(configuration);


        //app name
        if (!mapByKey.containsKey(SchemaConstant.APP_NAME)) {
            configuration = new SystemConfiguration();
            configuration.setConfigurationKey(SchemaConstant.APP_NAME);
            configuration.setConfigurationName("Application Name");
            configuration.setValue(SchemaConstant.DEFAULT_APP_NAME);
            configuration.setConfigurationGroup(GENERAL);
            configuration.setSortOrder(2);
            this.create(configuration);
        }
        //password reset validity term
        if (!mapByKey.containsKey(SchemaConstant.PSSWORD_SETTING_TOKEN_VLDTY_TRM)) {
            configuration = new SystemConfiguration();
            configuration.setConfigurationKey(SchemaConstant.PSSWORD_SETTING_TOKEN_VLDTY_TRM);
            configuration.setConfigurationName("Password reset token validity term");
            configuration.setValue(String.valueOf(SchemaConstant.DEFAULT_PWRD_SETTING_VLDTY_TRM));
            configuration.setDescription("Maximum no of hours reset token should be active before expired. Default to" +
                    " 168 (7 days)");
            configuration.setConfigurationGroup(GENERAL);
            configuration.setSortOrder(3);
            this.create(configuration);
        }

        //hear about us
        if (!mapByKey.containsKey(SchemaConstant.HEAR_ABT_US)) {
            configuration = new SystemConfiguration();
            configuration.setConfigurationKey(SchemaConstant.HEAR_ABT_US);
            configuration.setConfigurationName("Hear about us");
            configuration.setDescription("Set up for hear about us items. Each hear about us item must be seperated by comma");
            configuration.setValue("Web,Newspaper,Television");
            configuration.setConfigurationGroup(GENERAL);
            configuration.setSortOrder(4);
            this.create(configuration);
        }

        //emails
        if (!mapByKey.containsKey(SchemaConstant.SUPPORT_EMAIL)) {
            configuration = new SystemConfiguration();
            configuration.setConfigurationKey(SchemaConstant.SUPPORT_EMAIL);
            configuration.setConfigurationName("Support email");
            configuration.setValue(SchemaConstant.DEFAULT_SUPPORT_EMAIL);
            configuration.setDescription("Support email use for support and other administrative purposes");
            configuration.setConfigurationGroup(EMAIL);
            configuration.setSortOrder(1);
            this.create(configuration);
        }

        if (!mapByKey.containsKey(SchemaConstant.EMAIL_SUBJ_CREATE_CUSTOMER_USER_EMAIL)) {
            configuration = new SystemConfiguration();
            configuration.setConfigurationKey(SchemaConstant.EMAIL_SUBJ_CREATE_CUSTOMER_USER_EMAIL);
            configuration.setConfigurationName("Create customer user email subject");
            configuration.setValue(SchemaConstant.DEFAULT_CREATE_ADMIN_USER_EMAIL_SUBJ);
            configuration.setConfigurationGroup(EMAIL);
            configuration.setSortOrder(4);
            this.create(configuration);
        }

        if (!mapByKey.containsKey(SchemaConstant.EMAIL_SUBJ_CREATE_PSSWORD_EMAIL)) {
            configuration = new SystemConfiguration();
            configuration.setConfigurationKey(SchemaConstant.EMAIL_SUBJ_CREATE_PSSWORD_EMAIL);
            configuration.setConfigurationName("Create password email subject");
            configuration.setValue(SchemaConstant.DEFAULT_CREATE_PASSWORD_EMAIL_SUBJ);
            configuration.setConfigurationGroup(EMAIL);
            configuration.setSortOrder(4);
            this.create(configuration);
        }

        if (!mapByKey.containsKey(SchemaConstant.EMAIL_SUBJ_RESET_PSSWORD_EMAIL)) {
            configuration = new SystemConfiguration();
            configuration.setConfigurationKey(SchemaConstant.EMAIL_SUBJ_RESET_PSSWORD_EMAIL);
            configuration.setConfigurationName("DLE Reset password email subject");
            configuration.setValue(SchemaConstant.DEFAULT_PASSWORD_RESET_EMAIL_SUBJ);
            configuration.setConfigurationGroup(EMAIL);
            configuration.setSortOrder(4);
            this.create(configuration);
        }


        if (!mapByKey.containsKey(SchemaConstant.EMAIL_SUBJ_PASSWORD_CREATED_MAIL)) {
            configuration = new SystemConfiguration();
            configuration.setConfigurationKey(SchemaConstant.EMAIL_SUBJ_PASSWORD_CREATED_MAIL);
            configuration.setConfigurationName("Password created email subject");
            configuration.setValue(SchemaConstant.DEFAULT_PASSWORD_CREATED_MAIL_SUBJ);
            configuration.setConfigurationGroup(EMAIL);
            configuration.setSortOrder(6);
            this.create(configuration);
        }

        //email config
        if (!mapByKey.containsKey(SchemaConstant.EMAIL_CONFIG)) {
            EmailConfig emailConfig = new EmailConfig();
            emailConfig.setHost(env.getProperty("spring.mail.host"));
            emailConfig.setProtocol(env.getProperty("spring.mail.properties.mail.transport.protocol"));
            emailConfig.setPort(env.getProperty("spring.mail.properties.mail.smtp.port"));
            emailConfig.setSmtpAuth(Objects.equals(env.getProperty("spring.mail.properties.mail.smtp.auth"), "true"));
            emailConfig.setSsl(Objects.equals(env.getProperty("spring.mail.properties.mail.smtp.ssl.enable"), "true"));
            emailConfig.setUsername(env.getProperty("spring.mail.username"));
            emailConfig.setPassword(env.getProperty("spring.mail.password"));

            configuration = new SystemConfiguration();
            configuration.setConfigurationKey(SchemaConstant.EMAIL_CONFIG);
            configuration.setConfigurationName("Email Configuration");
            configuration.setDescription("Set up default email configuration");
            configuration.setValue(emailConfig.toJSONString());

//            if (configuration.getValue().length() > MAX_LENGTH) {
//                throw new DataTooLongException("The value exceeds the maximum allowed length.");
//            }
            this.create(configuration);

            this.create(configuration);
        }
    }

    @Override
    public Collection<SystemConfigurationDto> fetchSystemConfigurations(String group) {
        Collection<SystemConfiguration> systemConfigurations = this.repository.findByConfigurationGroupOrderBySortOrderAsc(group);
        return this.convertModelsToDto(systemConfigurations);
    }

    @Override
    @Transactional
    public void updateSystemConfigurations(String group, Map<String, String> dto) {
        Collection<SystemConfiguration> systemConfigurations = this.repository.findByConfigurationGroupOrderBySortOrderAsc(group);
        Map<String, SystemConfiguration> mapByKey = this.mapConfigurationsByKey(systemConfigurations);
        dto.forEach((key, value) -> {
            if (mapByKey.containsKey(key)) {
                mapByKey.get(key).setValue(value);
            }
        });
    }

    private Map<String, SystemConfiguration> mapConfigurationsByKey(Collection<SystemConfiguration> systemConfigurations) {
        Map<String, SystemConfiguration> mapByKey = null;
        if (CollectionUtils.isNotEmpty(systemConfigurations)) {
            mapByKey = new HashMap<>();
            for (SystemConfiguration config : systemConfigurations) {
                mapByKey.put(config.getConfigurationKey(), config);
            }
        }
        return mapByKey == null ? Collections.emptyMap() : mapByKey;
    }

    private Collection<SystemConfigurationDto> convertModelsToDto(Collection<SystemConfiguration> systemConfigurations) {

        Collection<SystemConfigurationDto> dtos = new ArrayList<>();

        for (SystemConfiguration systemConfiguration : systemConfigurations) {
            SystemConfigurationDto dto = new SystemConfigurationDto();
            dto.setDescription(systemConfiguration.getDescription());
            dto.setKey(systemConfiguration.getConfigurationKey());
            dto.setName(systemConfiguration.getConfigurationName());
            dto.setValue(systemConfiguration.getValue());
            dto.setType(systemConfiguration.getSystemConfigrationType().name());
            ConfigurationMetadata metadata = new ConfigurationMetadata();
            dto.setMetadata(metadata);
            dtos.add(dto);
        }
        return dtos;
    }
}
