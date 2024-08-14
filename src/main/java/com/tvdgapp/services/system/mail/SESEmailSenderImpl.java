//package com.tvdgapp.services.system.mail;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Profile;
//import org.springframework.lang.Nullable;
//import org.springframework.stereotype.Component;
//
//@Component("sesEmailSender")
//@Profile({"prod"})
//public class SESEmailSenderImpl implements EmailSender {
//
//    // The email body for recipients with non-HTML email clients.
//    static final String TEXTBODY =
//            "This email was sent through Amazon SES " + "using the AWS SDK for Java.";
//
//    @Value("${amazonProperties.region}")
//    private @Nullable String region;
//    @Value("${amazonProperties.accessKey}")
//    private @Nullable String accessKey;
//    @Value("${amazonProperties.secretKey}")
//    private @Nullable String secretKey;
//
//    @Override
//    public void send(Email email) {
//
//        if(this.accessKey==null){
//            throw new InvalidPropertyConfigurationException("[Aws access key-null]");
//        }
//
//        if(this.secretKey==null){
//            throw new InvalidPropertyConfigurationException("[AWS secret-null]");
//        }
//
//        if(region==null){
//            throw new InvalidPropertyConfigurationException("[AWS region-null]");
//        }
//
//        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
//
//        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
//                // Replace US_WEST_2 with the AWS Region you're using for
//                // Amazon SES.
//                // .withRegion(Regions.valueOf(region.toUpperCase())).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
//                .withRegion(Regions.valueOf(region.toUpperCase())).build();
//        SendEmailRequest request = new SendEmailRequest()
//                .withDestination(new Destination().withToAddresses(email.getTo()))
//                .withMessage(new Message()
//                        .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(email.getBody()))
//                                .withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
//                        .withSubject(new Content().withCharset("UTF-8").withData(email.getSubject())))
//                .withSource(email.getFromEmail());
//        // Comment or remove the next line if you are not using a
//        // configuration set
//        //.withConfigurationSetName(CONFIGSET);
//
//        client.sendEmail(request);
//
//    }
//
//    @Override
//    public void setEmailConfig(EmailConfig emailConfig) {
//
//    }
//}
