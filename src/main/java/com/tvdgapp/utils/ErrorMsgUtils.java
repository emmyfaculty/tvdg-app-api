package com.tvdgapp.utils;

import java.text.MessageFormat;
import java.util.HashMap;

public class ErrorMsgUtils {

    public static final HashMap<String, String> ERR_MSGS = new HashMap<>();

    static {
        ERR_MSGS.put("organization.not.found", "Organisation with id - {0} does not exist.");
        ERR_MSGS.put("organization.duplicate", "Organisation with code - {0} already exist.");
        ERR_MSGS.put("industry.not.found", "Industry with id - {0} does not exist.");
        ERR_MSGS.put("country.not.found", "Country with id - {0} does not exist.");
        ERR_MSGS.put("role.not.found", "Role with key - {0} does not exist.");
        ERR_MSGS.put("customer_user.not.found", "User with id - {0} does not exist.");
        ERR_MSGS.put("customer_user.duplicate", "User with email - {0} already exist.");
        ERR_MSGS.put("package.not.found", "Package with id - {0} does not exist.");
        ERR_MSGS.put("package.duplicate", "Package with code - {0} already exist.");
        ERR_MSGS.put("subscription.not.found", "Subscription with id - {0} does not exist.");
        ERR_MSGS.put("squad.not.found", "Squad with id - {0} does not exist.");
        ERR_MSGS.put("techstack.not.found", "Tech Stack with id - {0} does not exist.");
        ERR_MSGS.put("engineer.not.found", "Engineer with id - {0} does not exist.");
        ERR_MSGS.put("engineer_assignment.not.found", "Engineer Assignment with id - {0} does not exist.");
        ERR_MSGS.put("engineer_report.not.found", "Report with id - {0} does not exist.");
        ERR_MSGS.put("engineer_assignment_comment.not.found", "Comment with id - {0} does not exist.");
        ERR_MSGS.put("user.not.found", "User with id - {0} does not exist.");
        ERR_MSGS.put("admin_user.not.found", "User with id - {0} does not exist.");
        ERR_MSGS.put("admin_user.duplicate", "User with email - {0} already exist.");
        ERR_MSGS.put("affiliate_user.duplicate", "User with email - {0} already exist.");
        ERR_MSGS.put("engineer_assignment_request.not.found", "Request with id - {0} does not exist.");
        ERR_MSGS.put("subscription_request.not.found", "Request with id - {0} does not exist.");
        ERR_MSGS.put("invoice.not.found", "Invoice with id - {0} does not exist.");
        ERR_MSGS.put("billing_rate.not.found", "Billing rate with id - {0} does not exist.");
        ERR_MSGS.put("batch.not.found", "Batch with id - {0} does not exist.");

    }

    public static String formatMsg(String entityName,String exceptionType, String ...args) {
        String templateKey=entityName.concat(".").concat(exceptionType).toLowerCase();
        String template=ERR_MSGS.get(templateKey);
        if(template!=null) {
            return MessageFormat.format(template, (Object[]) args);
        }
       return templateKey;
    }
}
