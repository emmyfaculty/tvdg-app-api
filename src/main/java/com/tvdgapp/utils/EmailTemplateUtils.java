package com.tvdgapp.utils;

import com.tvdgapp.constants.Constants;
import com.tvdgapp.constants.EmailConstants;
import com.tvdgapp.constants.SchemaConstant;
import com.tvdgapp.dtos.notification.NotificationRequestDto;
import com.tvdgapp.models.fileclaims.FileClaims;
import com.tvdgapp.models.user.User;
import com.tvdgapp.models.user.admin.AdminUser;
import com.tvdgapp.models.user.affiliateuser.AffiliateUser;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.repositories.User.UserRepository;
import com.tvdgapp.services.system.SystemConfigurationService;
import com.tvdgapp.services.system.mail.Email;
import com.tvdgapp.services.system.mail.EmailService;
import com.tvdgapp.services.system.mail.templateEngine.TemplateEngine;
import com.tvdgapp.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
@SuppressWarnings("NullAway.Init")
public class EmailTemplateUtils {

    private final static String LOGOPATH = "LOGOPATH";

    private final EmailService emailService;
    private final SystemConfigurationService systemConfigurationService;
    private final TemplateEngine templateEngine;
//    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${api.url-domain}")
    private String urlDomain;

    private final static String EMAIL_FOOTER_COPYRIGHT = "EMAIL_FOOTER_COPYRIGHT";
    private final static String EMAIL_DISCLAIMER = "EMAIL_DISCLAIMER";
    private final static String EMAIL_SPAM_DISCLAIMER = "EMAIL_SPAM_DISCLAIMER";
    private final static String EMAIL_FROM_EMAIL = "EMAIL_FROM_EMAIL";


    public void sendCreatePasswordUrlLinkEmail(User user, String passwordUrl) {

        Map<String, Object> templateTokens = this.createEmailObjectsMap();
        templateTokens.put(Constants.CREATE_PASSWORD_URL, passwordUrl);
        templateTokens.put(EmailConstants.EMAIL_FULLNAME, user.getLastName() + " " + user.getFirstName());

        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_CREATE_PSSWORD_EMAIL);
        if (StringUtils.isEmpty(subject)) {
            subject = SchemaConstant.DEFAULT_CREATE_PASSWORD_EMAIL_SUBJ;
            log.warn("Found no Create password email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
        }

        Email email = this.createEmail(user.getEmail(), subject, EmailConstants.EMAIL_CREATE_PASSWORD_TMPL, templateTokens);
        this.emailService.sendAsyncEmail(email);
    }

    public void sendPasswordResetEmail(User user, String passwordResetUrl) {

        Map<String, Object> templateTokens = this.createEmailObjectsMap();
        templateTokens.put(EmailConstants.EMAIL_PASSWORD_RESET_URL, passwordResetUrl);
        templateTokens.put(EmailConstants.EMAIL_FULLNAME, user.getLastName() + " " + user.getFirstName());
        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_RESET_PSSWORD_EMAIL);
        if (StringUtils.isEmpty(subject)) {
            subject = SchemaConstant.DEFAULT_PASSWORD_RESET_EMAIL_SUBJ;
            log.warn("Found no password reset email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
        }

        Email email = this.createEmail(user.getEmail(), subject, EmailConstants.EMAIL_PASSWORD_RESET_TMPL, templateTokens);
        this.emailService.sendAsyncEmail(email);
    }

    /**
     * Builds generic email information
     *
     * @return
     */
    private Map<String, Object> createEmailObjectsMap() {

        String companyName = systemConfigurationService.findConfigValueByKey(SchemaConstant.COMPANY_NAME);
        if (StringUtils.isEmpty(companyName)) {
            log.warn("No Company name found in configuration,using default application defined value");
            companyName = SchemaConstant.DEFAULT_COMPANY_NAME;
        }
        String[] copyArg = {companyName, TvdgAppDateUtils.getPresentYear()};

        String supportEmail = systemConfigurationService.findConfigValueByKey(SchemaConstant.SUPPORT_EMAIL);
        if (StringUtils.isEmpty(supportEmail)) {
            log.warn("No Support email found in configuration,using default application defined value");
            supportEmail = SchemaConstant.DEFAULT_SUPPORT_EMAIL;
        }
        String[] supportEmailArg = {supportEmail};

        String emailDisclaimer = "If you feel that you have received this email in error, please send an email to " + supportEmailArg[0] + " for de-activation";
        String emailSpamDisclaimer = "This email is sent in accordance with the US CAN-SPAM Law in effect 2004-01-01. Removal requests can be sent to this address and will be honored and respected";

        Map<String, Object> templateTokens = new HashMap<>();

        templateTokens.put(LOGOPATH, "");
        templateTokens.put(EMAIL_FOOTER_COPYRIGHT, "Copyright @ " + copyArg[0] + " " + copyArg[1] + ", All Rights Reserved");
        templateTokens.put(EMAIL_DISCLAIMER, emailDisclaimer);
        templateTokens.put(EMAIL_SPAM_DISCLAIMER, emailSpamDisclaimer);
        templateTokens.put(EMAIL_FROM_EMAIL, supportEmail);

        return templateTokens;
    }

    private Email createEmail(String to, String subject, String template, Map<String, Object> templateTokens) {

        Email email = new Email();

        String companyName = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.COMPANY_NAME);
        if (StringUtils.isEmpty(companyName)) {
            companyName = SchemaConstant.DEFAULT_COMPANY_NAME;
            log.warn("Found no Company Name defined in configuration. Using application defined name:" + companyName + " as a fallback");
        }

        /*String companyEmail = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.COMPANY_EMAIL);
        if (!CommonUtils.isValidEmail(companyEmail)) {
            log.warn("Found invalid Company Email:'" + companyEmail + "' defined in configuration. Using application defined email:" + SchemaConstant.DEFAULT_COMPANY_EMAIL + " as a fallback");
            companyEmail = SchemaConstant.DEFAULT_COMPANY_EMAIL;
        }*/
        email.setFrom(companyName);
        email.setFromEmail((String) templateTokens.get(EMAIL_FROM_EMAIL));
        email.setSubject(subject);
        email.setTo(to);
        email.setBody(this.templateEngine.processTemplateIntoString(template, templateTokens));
        return email;
    }

//    private Email createEmailWithAttachment(String to, String subject, String template, Map<String, Object> templateTokens, String attachmentFileName, InputStream attachment) throws IOException {
//        Email email = this.createEmail(to, subject, template, templateTokens);
//        FileAttachement fileAttachment = new FileAttachement(attachmentFileName, attachment);
//        email.setAttachement(fileAttachment);
//        return email;
//    }
    public void sendCustomerPasswordCreatedEmail(CustomerUser user, String engineerUrl) {
        Map<String, Object> templateTokens = this.createEmailObjectsMap();
        templateTokens.put(EmailConstants.ENGINEER_URL, engineerUrl + "/login");
        templateTokens.put(EmailConstants.EMAIL_USER_NAME, user.getEmail());
        templateTokens.put(EmailConstants.EMAIL_FULLNAME, user.getLastName() + " " + user.getFirstName());
        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_PASSWORD_CREATED_MAIL);
        if (StringUtils.isEmpty(subject)) {
            subject = SchemaConstant.DEFAULT_PASSWORD_CREATED_MAIL_SUBJ;
            log.warn("Found no password created email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
        }
        Email email = this.createEmail(user.getEmail(), subject, EmailConstants.EMAIL_ENGINEER_PASSWORD_CREATED_TMPL, templateTokens);
        this.emailService.sendAsyncEmail(email);
    }

    public void sendCreatedCustomerEmail(User user, String plainPassword, String loginUrl) {
        Map<String, Object> templateTokens = this.createEmailObjectsMap();
//        templateTokens.put(EmailConstants.EMAIL_USER_PASSWORD, user.getPassword());
        templateTokens.put(EmailConstants.EMAIL_USER_PASSWORD, plainPassword);
        templateTokens.put(EmailConstants.EMAIL_FULLNAME, user.getLastName() + " " + user.getFirstName());
        templateTokens.put(EmailConstants.EMAIL_USER_NAME, user.getEmail());
        templateTokens.put("APP_URL", loginUrl);
        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_CREATE_CUSTOMER_USER_EMAIL);
        if (StringUtils.isEmpty(subject)) {
            subject = SchemaConstant.DEFAULT_CREATE_ADMIN_USER_EMAIL_SUBJ;
            log.warn("Found no Create password email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
        }

        Email email = this.createEmail(user.getEmail(), subject, EmailConstants.EMAIL_CREATE_CUSTOMER_USER_TMPL, templateTokens);
        this.emailService.sendAsyncEmail(email);

    }
    public void sendCreateOtpEmail(User user, String otp) {
        Map<String, Object> templateTokens = this.createEmailObjectsMap();
        templateTokens.put(EmailConstants.EMAIL_OTP, otp);
//        templateTokens.put(EmailConstants.EMAIL_FULLNAME, user.getLastName() + " " + user.getFirstName());
//        templateTokens.put(EmailConstants.EMAIL_USER_NAME, user.getEmail());
        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_CREATE_RIDER_LOGIN_OTP_EMAIL);
        if (StringUtils.isEmpty(subject)) {
            subject = SchemaConstant.DEFAULT_CREATE_RIDER_LOGIN_OTP_EMAIL_SUBJ;
            log.warn("Found no Create otp email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
        }

        Email email = this.createEmail(user.getEmail(), subject, EmailConstants.EMAIL_CREATE_RIDER_LOGIN_OTP_TMPL, templateTokens);
        this.emailService.sendAsyncEmail(email);

    }

    public void sendFileClaimRequestEmail(String toEmailAddress, FileClaims fileClaims) {

        Map<String, Object> templateTokens = this.createEmailObjectsMap();
        templateTokens.put(EmailConstants.EMAIL_ORGANISATION_NAME, fileClaims.getCompanyName());
        templateTokens.put(EmailConstants.EMAIL_TRACKING_NUMBER, fileClaims.getShipment().getTrackingNumber());
        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_SUBSCRIPTION_REQUEST_EMAIL);
        if (StringUtils.isEmpty(subject)) {
            subject = SchemaConstant.DEFAULT_FILE_CLAIM_REQUEST_EMAIL_SUBJ;
            log.warn("Found no File Claim request email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
        }
        Email email = this.createEmail(toEmailAddress, subject, EmailConstants.EMAIL_FILE_CLAIM_REQUEST_TMPL, templateTokens);
        this.emailService.sendAsyncEmail(email);
    }

    public void sendCreateAdminUserEmail(AdminUser adminUser, String plainPassword, String loginUrl) {
        Map<String, Object> templateTokens = this.createEmailObjectsMap();
        templateTokens.put(EmailConstants.EMAIL_USER_PASSWORD, plainPassword);
        templateTokens.put(EmailConstants.EMAIL_FULLNAME, adminUser.getLastName() + " " + adminUser.getFirstName());
        templateTokens.put(EmailConstants.EMAIL_USER_NAME, adminUser.getEmail());
        templateTokens.put("APP_URL", loginUrl);

        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_CREATE_ADMIN_USER_EMAIL);
        if (StringUtils.isEmpty(subject)) {
            subject = SchemaConstant.DEFAULT_CREATE_ADMIN_USER_EMAIL_SUBJ;
            log.warn("Found no Create password email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
        }

        Email email = this.createEmail(adminUser.getEmail(), subject, EmailConstants.EMAIL_CREATE_ADMIN_USER_TMPL, templateTokens);
        this.emailService.sendAsyncEmail(email);
    }
    public void sendPendingAffiliateUserEmail(AffiliateUser affiliateUser, String plainPassword, String loginUrl) {
        Map<String, Object> templateTokens = this.createEmailObjectsMap();
        templateTokens.put(EmailConstants.EMAIL_USER_PASSWORD, plainPassword);
        templateTokens.put(EmailConstants.EMAIL_FULLNAME, affiliateUser.getLastName() + " " + affiliateUser.getFirstName());
        templateTokens.put(EmailConstants.EMAIL_USER_NAME, affiliateUser.getEmail());
        templateTokens.put(EmailConstants.EMAIL_USER_REFERRAL_CODE, affiliateUser.getReferralCode());
//        templateTokens.put("APP_URL", urlDomain + "/login");
        templateTokens.put("APP_URL", loginUrl);

        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_CREATE_AFFILIATE_USER_EMAIL);
        if (StringUtils.isEmpty(subject)) {
            subject = SchemaConstant.DEFAULT_CREATE_AFFILIATE_USER_EMAIL_SUBJ;
            log.warn("Found no Create password email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
        }

        Email email = this.createEmail(affiliateUser.getEmail(), subject, EmailConstants.EMAIL_CREATE_AFFILIATE_USER_TMPL, templateTokens);
        this.emailService.sendAsyncEmail(email);
    }


//    public void sendCreatedCustomerEmail(User user, String plainPassword, String loginUrl) {
//        Map<String, Object> templateTokens = this.createEmailObjectsMap();
////        templateTokens.put(EmailConstants.EMAIL_USER_PASSWORD, user.getPassword());
//        templateTokens.put(EmailConstants.EMAIL_USER_PASSWORD, plainPassword);
//        templateTokens.put(EmailConstants.EMAIL_FULLNAME, user.getLastName() + " " + user.getFirstName());
//        templateTokens.put(EmailConstants.EMAIL_USER_NAME, user.getEmail());
//        templateTokens.put("APP_URL", loginUrl);
//        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_CREATE_CUSTOMER_USER_EMAIL);
//        if (StringUtils.isEmpty(subject)) {
//            subject = SchemaConstant.DEFAULT_CREATE_ADMIN_USER_EMAIL_SUBJ;
//            log.warn("Found no Create password email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
//        }
//
//        Email email = this.createEmail(user.getEmail(), subject, EmailConstants.EMAIL_CREATE_CUSTOMER_USER_TMPL, templateTokens);
//        this.emailService.sendAsyncEmail(email);
//
//    }



    public void sendNotifyAdminOfNewAffiliateUserEmail(String emailAddy,AffiliateUser affiliateUser) {
        Map<String, Object> templateTokens = this.createEmailObjectsMap();
        templateTokens.put(EmailConstants.EMAIL_FULLNAME, affiliateUser.getLastName() + " " + affiliateUser.getFirstName());
        templateTokens.put(EmailConstants.EMAIL_USER_NAME, affiliateUser.getEmail());

        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_NOTIFY_ADMIN_USER_NEW_AFFILIATE_USER_EMAIL);
        if (StringUtils.isEmpty(subject)) {
            subject = SchemaConstant.EMAIL_SUBJ_NOTIFY_ADMIN_USER_NEW_AFFILIATE_USER_EMAIL_SUBJ;
            log.warn("Found no Notify Admin email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
        }

        Email email = this.createEmail(emailAddy, subject, EmailConstants.EMAIL_CREATE_NOTIFY_ADMIN_NEW_AFFILIATE_USER_TMPL, templateTokens);
        this.emailService.sendAsyncEmail(email);
    }
    public void sendAffiliateUserApprovalEmail(AffiliateUser affiliateUser) {
        Map<String, Object> templateTokens = this.createEmailObjectsMap();
        templateTokens.put(EmailConstants.EMAIL_FULLNAME, affiliateUser.getLastName() + " " + affiliateUser.getFirstName());

        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_AFFILIATE_USER_APPROVAL_EMAIL);
        if (StringUtils.isEmpty(subject)) {
            subject = SchemaConstant.EMAIL_SUBJ_SEND_AFFILIATE_USER_APPROVAL_EMAIL_SUBJ;
            log.warn("Found no send affiliate user approval email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
        }

        Email email = this.createEmail(affiliateUser.getEmail(), subject, EmailConstants.EMAIL_SEND_AFFILIATE_USER_APPROVAL_TMPL, templateTokens);
        this.emailService.sendAsyncEmail(email);
    }

    public void sendEmailNotification(NotificationRequestDto notificationRequestDto, String role) {
                List<String> targets = notificationRequestDto.getTargets();
        if (targets == null || targets.isEmpty()) {
            targets = this.getUserEmailsByRole(role);
        }

        Map<String, Object> templateTokens = this.createEmailObjectsMap();
        templateTokens.put(EmailConstants.EMAIL_TITLE, notificationRequestDto.getTitle());
        templateTokens.put(EmailConstants.EMAIL_MESSAGE, notificationRequestDto.getMessage());

        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_NOTIFICATION_EMAIL);
        if (StringUtils.isEmpty(subject)) {
            subject = SchemaConstant.EMAIL_SUBJ_SEND_NOTIFICATION_EMAIL_SUBJ;
            log.warn("Found no send notification email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
        }
//
        for (String target : targets) {

        Email email = this.createEmail(target, subject, EmailConstants.EMAIL_SEND_NOTIFICATION_TMPL, templateTokens);
        this.emailService.sendAsyncEmail(email);
//            emailService.sendEmail(target, notificationRequestDto.getTitle(), notificationRequestDto.getMessage());
        }

    }

    public List<String> getUserEmailsByRole(String role) {
        return userRepository.findEmailsByRoleName(role);
    }

//    public void sendEnquiryNotificationEmail(EnquiryNotificationDto enquiryNotificationDto, String recipientEmail) {
//        Map<String, Object> templateTokens = this.createEmailObjectsMap();
//        templateTokens.put(EmailConstants.EMAIL_ENQUIRER_NAME, enquiryNotificationDto.getName());
//        templateTokens.put(EmailConstants.EMAIL_ENQUIRER_EMAIL, enquiryNotificationDto.getEmail());
//        templateTokens.put(EmailConstants.EMAIL_ENQUIRER_PHONE_NO, enquiryNotificationDto.getPhoneNo());
//        templateTokens.put(EmailConstants.EMAIL_ENQUIRER_COMPANY_NAME, enquiryNotificationDto.getCompanyName());
//        templateTokens.put(EmailConstants.EMAIL_ENQUIRER_JOB_TITLE, enquiryNotificationDto.getJobTitle());
//        templateTokens.put(EmailConstants.EMAIL_ENQUIRER_INDUSTRY, enquiryNotificationDto.getIndustry());
//        templateTokens.put(EmailConstants.EMAIL_ENQUIRER_PROGRAMMING_LANGUAGE_CHOICE, enquiryNotificationDto.getProgrammingLanguageChoice());
//        templateTokens.put(EmailConstants.EMAIL_ENQUIRER_ENGINEER_NO, enquiryNotificationDto.getNoOfEngineers());
//        templateTokens.put(EmailConstants.EMAIL_ENQUIRER_DESCRIPTION, enquiryNotificationDto.getEnquiryDescription());
//        String subject = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.EMAIL_SUBJ_ENQUIRY_NOTIFICATION_EMAIL);
//        if (StringUtils.isEmpty(subject)) {
//            subject = SchemaConstant.DEFAULT_ENQUIRY_NOTIFICATION_EMAIL_SUBJ;
//            log.warn("Found no enquiry notification email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
//        }
//        Email email = this.createEmail(recipientEmail, subject, EmailConstants.EMAIL_ENQUIRY_NOTIFICATION_TMPL, templateTokens);
//        this.emailService.sendEmail(email);
//
//    }
//
}

