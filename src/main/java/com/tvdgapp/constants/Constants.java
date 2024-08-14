package com.tvdgapp.constants;

public class Constants {

    public static final String EMAIL_REGEX = "^\\s*|((?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,}))$";

    public static final String CUSTOMER_USER_PROFILE_PIC_DIR = "avatars";
    public static final String PROFILE_PIC_DIR = "profilePics";
    public static final String FILE_CLAIMS_RECEIPT_DIR = "fileClaimsReceipt";
    public static final int AVATAR_THUMBNAIL_WIDTH = 50;
    public static final int AVATAR_THUMBNAIL_HEIGHT = 50;
    public static final String ROLE_PREFIX = "";
    public static final String USER_TYPE = "user_type";
    public final static String AUTHORITIES_CLAIM_KEY_SEP = ",";
    public static final String ROLE_SUPER_ADMIN = "SUPERADMIN";

    public static final String ROLE_TYPE_ADMIN = "admin";
    public static final String ROLE_TYPE_CUSTOMER = "customer";

    public static final String ROLE_TYPE_RIDER = "ROLE_RIDER";
    public static final String ROLE_TYPE_CUSTOMER_SERVICE_EXECUTIVE = "ROLE_CUSTOMER_SERVICE_EXECUTIVE";
    public static final String ROLE_TYPE_FINANCE = "ROLE_FINANCE";
    public static final String ROLE_TYPE_RIDER_DISPATCH = "ROLE_RIDER_DISPATCH";


    public static final String ROLE_ADMIN_MEMBER = "ADMINMEMBER";

    public final static String TRUE = "true";
    public final static String FALSE = "false";
    public static final String EMAIL = "Email";
    public static final String ENGINEER_URI = "/engineer";
    public static final String SPECIFICATION_KEY_SEP = ",";
    public static final String PWORD_RST_TKN_PATTERN = "^[A-Za-z0-9]*$";
    //create pasword
    public static final String PASSWORD_URL = "/createpassword";
    public static final String PASSWORD_URL_TOKEN_PARAM = "token=";
    public static final String CREATE_PASSWORD_URL = "CREATE_PASSWORD_URL";
    public static final String PASSWORD_RESET_URL = "/resetpassword";
    public static final String CUSTOMER_URI = "/customer";
    public static final String AFFILIATE_URI = "/affiliate";
    public static final String SUBSCRIPTION_RECEIPTS_DIR = "fileclaims/receipts";

    public static final String ROLE_TYPE_AFFILIATE = "affiliate";
    public static final String ROLE_AFFILIATE = "AFFILIATE";
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String ADMIN_URI ="/admin" ;
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_FINANCE = "FINANCE";
    public static final String ROLE_CUSTOMER_SERVICE_EXECUTIVE = "CSE";
    public static final String ROLE_RIDER = "RIDER" ;
}
