package com.tvdgapp.services.system.mail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@RequiredArgsConstructor
@Component("defaultEmailSenderImpl")
@Profile("!prod")
public class DefaultEmailSenderImpl implements EmailSender {

    private final JavaMailSender emailSender;
    private   @Nullable EmailConfig emailConfig;


    @Override
    public void send(Email email){

        log.info("Begin sending email ");
        final String eml = email.getFrom();
        final String from = email.getFromEmail();
        final String to = email.getTo();
        final String subject = email.getSubject();
        //final FileAttachement attachement = email.getAttachement();
        Objects.requireNonNull(to);

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws MessagingException, IOException {

                JavaMailSenderImpl impl = (JavaMailSenderImpl) emailSender;
                // if email configuration is present in Database, use the same
                if (emailConfig != null) {

                    Objects.requireNonNull(emailConfig.getPort());

                    impl.setProtocol(emailConfig.getProtocol());
                    impl.setHost(emailConfig.getHost());
                    impl.setPort(Integer.parseInt(emailConfig.getPort()));
                    impl.setUsername(emailConfig.getUsername());
                    impl.setPassword(emailConfig.getPassword());

                    Properties prop = new Properties();
                    prop.put("mail.smtp.auth", emailConfig.isSmtpAuth());
                    prop.put("mail.smtp.ssl.enable", emailConfig.isSsl());
                    impl.setJavaMailProperties(prop);
                }


                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

                InternetAddress inetAddress = new InternetAddress();

                inetAddress.setPersonal(eml);
                inetAddress.setAddress(from);

                mimeMessage.setFrom(inetAddress);
                mimeMessage.setSubject(subject);
                mimeMessage.setText(email.getBody(), "utf-8", "html");

                if(email.getAttachement()!=null){
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
                    messageHelper.addAttachment(email.getAttachement().getFileName(), new ByteArrayResource(email.getAttachement().getInputStream().readAllBytes()));
                    if(email.getBody()!=null) {
                        messageHelper.setText(email.getBody(), true);
                    }
                }

            }
        };
        emailSender.send(preparator);
    }

    @Override
    public void setEmailConfig(@Nullable EmailConfig emailConfig) {
        this.emailConfig=emailConfig;
    }
}
