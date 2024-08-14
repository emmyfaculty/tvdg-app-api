package com.tvdgapp.services.system.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvdgapp.constants.SchemaConstant;
import com.tvdgapp.services.system.SystemConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.tvdgapp.utils.CommonUtils.castToNonNull;


@Slf4j
@RequiredArgsConstructor
@Service("emailService")
public class EmailServiceImpl implements EmailService {

    private final EmailSender emailSender;
    private final SystemConfigurationService systemConfigurationService;

    @Override
    public void sendEmail(Email email) {
        this.send(email);
    }

    @Override
    @Async
    public void sendAsyncEmail(Email email) {
        this.send(email);
    }

    private void send(Email email) {
        if (StringUtils.isEmpty(email.getFromEmail()) || StringUtils.isEmpty(email.getTo())) {
            log.error("Cannot send email: Invalid email address- source:" + email.getFromEmail() + " | destination:" + email.getTo());
            return;
        }
        this.getEmailConfiguration().ifPresent(this.emailSender::setEmailConfig);
        this.emailSender.send(email);
    }


    private Optional<EmailConfig> getEmailConfiguration() {

        String value = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_CONFIG);
        ObjectMapper mapper = new ObjectMapper();
        EmailConfig emailConfig = null;
        try {
            emailConfig = mapper.readValue(value, EmailConfig.class);
        } catch (Exception e) {
            // throw new ServiceException("Cannot parse json string " + value);
        }
        return Optional.ofNullable(castToNonNull(emailConfig));
    }


}
