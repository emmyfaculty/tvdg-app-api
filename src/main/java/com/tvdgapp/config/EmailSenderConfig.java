package com.tvdgapp.config;

import com.tvdgapp.services.system.mail.EmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailSenderConfig {

    @Value("${config.emailSender}")
    private String emailSender;

    @Bean
    public EmailSender emailSender(ApplicationContext context) {
        switch (emailSender) {
            case "default":
                return (EmailSender) context.getBean("defaultEmailSenderImpl");
            case "cloud":
                return (EmailSender) context.getBean("sesEmailSender");
            case "cloud-smtp":
                return (EmailSender) context.getBean("sesSMTPEmailSender");
            default:
                return (EmailSender) context.getBean("defaultEmailSenderImpl");
        }
    }

}
