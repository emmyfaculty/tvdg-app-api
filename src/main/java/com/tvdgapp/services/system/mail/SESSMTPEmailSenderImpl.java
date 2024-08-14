//package com.tvdgapp.services.system.mail;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.lang.Nullable;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Objects;
//import java.util.Properties;
//
//@Slf4j
//@Component("sesSMTPEmailSender")
//@Profile({"prod"})
//public class SESSMTPEmailSenderImpl implements EmailSender {
//
//
//    // The name of the Configuration Set to use for this message.
//    // If you comment out or remove this variable, you will also need to
//    // comment out or remove the header below.
//    //static final String CONFIGSET = "ConfigSet";
//
//    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
//    // See https://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
//    // for more information.
//    @Value("${amazonProperties.smtpHost}")
//    private @Nullable String SMTP_HOST;
//
//    // Replace smtp_username with your Amazon SES SMTP user name.
//    @Value("${amazonProperties.smtpUserName}")
//    private @Nullable String SMTP_USERNAME;
//
//    // Replace smtp_password with your Amazon SES SMTP password.
//    @Value("${amazonProperties.smtpPassword}")
//    private @Nullable String SMTP_PASSWORD;
//
//    // The port you will connect to on the Amazon SES SMTP endpoint.
//    static final int PORT = 587;
//
//    @Override
//    public void send(Email email) {
//
//        Objects.requireNonNull(email.getTo());
//
//        Transport transport = null;
//        try {
//            log.info("Sending...");
//            // Create a Properties object to contain connection configuration information.
//            Properties props = System.getProperties();
//            props.put("mail.transport.protocol", "smtp");
//            props.put("mail.smtp.port", PORT);
//            props.put("mail.smtp.starttls.enable", "true");
//            props.put("mail.smtp.auth", "true");
//
//            // Create a Session object to represent a mail session with the specified properties.
//            Session session = Session.getDefaultInstance(props);
//
//            // Create a message with the specified information.
//            MimeMessage msg = new MimeMessage(session);
//            msg.setFrom(new InternetAddress(email.getFromEmail(), email.getFrom()));
//            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
//            msg.setSubject(email.getSubject());
//            msg.setContent(email.getBody(), "text/html");
//
//            if(email.getAttachement()!=null){
//                MimeMessageHelper messageHelper = new MimeMessageHelper(msg, true);
//                messageHelper.addAttachment(email.getAttachement().getFileName(), new ByteArrayResource(email.getAttachement().getInputStream().readAllBytes()));
//                if(email.getBody()!=null) {
//                    messageHelper.setText(email.getBody(), true);
//                }
//            }
//
//            // Add a configuration set header. Comment or delete the
//            // next line if you are not using a configuration set
//            //msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
//
//            // Create a transport.
//            transport = session.getTransport();
//
//            // Send the message.
//
//            // Connect to Amazon SES using the SMTP username and password you specified above.
//            transport.connect(SMTP_HOST, SMTP_USERNAME, SMTP_PASSWORD);
//
//            // Send the email.
//            transport.sendMessage(msg, msg.getAllRecipients());
//
//        } catch (Exception ex) {
//            log.error(ex.getMessage(), ex);
//        } finally {
//            // Close and terminate the connection.
//            try {
//                if (transport != null) {
//                    transport.close();
//                }
//            } catch (MessagingException e) {
//                log.error(e.getMessage(), e);
//            }
//        }
//    }
//
//
//    @Override
//    public void setEmailConfig(EmailConfig emailConfig) {
//
//    }
//}
