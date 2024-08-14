package com.tvdgapp.constants;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

public class SchemaConstant {

    public final static String FELLOWSHIP_SCHEMA = "TVDGAPP";
    public static final String DEFAULT_SUPER_ADMINISTRATOR_EMAIL = "aholemmy@gmail.com";
    public static final String DEFAULT_SUPER_ADMINISTRATOR_DEFAULT_PASS = "superadmin";
    public static final String DEFAULT_APP_NAME = "Tvdg Enterprise Application";
    public static final String DEFAULT_COMPANY_EMAIL = "aholemmy@gmail.com";//todo change later

    public static final String DEFAULT_INVOICE_COMPANY_NAME = "Decagon Services Inc";
    public static final String DEFAULT_INVOICE_COMPANY_ADDRESS = "1237 LONGHORN RANCH DRIVE, LEANDER TX, 78641 UNITED STATE OF AMERICA";
    public static final String DEFAULT_COMPANY_NAME = "Tvdg Enterprise Application";
    public static final String DEFAULT_CREATE_PASSWORD_EMAIL_SUBJ = "Create Password Email";
    public static final String DEFAULT_PASSWORD_CREATED_MAIL_SUBJ = "Password Created";
    public static final String DEFAULT_CREATE_ADMIN_USER_EMAIL_SUBJ = "Account created";
    public static final String DEFAULT_CREATE_RIDER_LOGIN_OTP_EMAIL_SUBJ = "OTP created";
    public static final String DEFAULT_CREATE_AFFILIATE_USER_EMAIL_SUBJ = "Account created";
    public static final String EMAIL_SUBJ_NOTIFY_ADMIN_USER_NEW_AFFILIATE_USER_EMAIL_SUBJ = "New Affiliate User Account Pending Verification";
    public static final String EMAIL_SUBJ_SEND_AFFILIATE_USER_APPROVAL_EMAIL_SUBJ = "Your Account Has Been Approved";
    public static final String EMAIL_SUBJ_SEND_NOTIFICATION_EMAIL_SUBJ = "Notification";
    public static final String DEFAULT_PASSWORD_RESET_EMAIL_SUBJ = "Reset Password";
    public static final String DEFAULT_FILE_CLAIM_REQUEST_EMAIL_SUBJ = "File Claim Request";
    public static final String DEFAULT_ENGINEER_REQUEST_EMAIL_SUBJ = "Engineer Request";
    public static final String DEFAULT_CANCEL_ENGINEER_REQUEST_LINE_ITEM_EMAIL_SUBJ = "Cancel Engineer Request Line Item";
    public static final int DEFAULT_PWRD_SETTING_VLDTY_TRM = 168;//in hours(7days)
    public final static Locale DEFAULT_LOCALE = Locale.US;
    public final static Currency DEFAULT_CURRENCY = Currency.getInstance("NGN");
    public static final String DEFAULT_COUNTRY = "NG";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String DEFAULT_ENGINEER_ASSIGNMENT_REQUEST_TO_EMAIL =  DEFAULT_COMPANY_EMAIL;
    public static final String DEFAULT_SUBSCRIPTION_REQUEST_TO_EMAIL =DEFAULT_COMPANY_EMAIL;
    public static final String DEFAULT_SUPPORT_EMAIL = DEFAULT_COMPANY_EMAIL;
    public static final String DEFAULT_SUBSCRIPTION_EXPIRATION_REMINDER_EMAIL_SUBJ = "Subscription Expiration Reminder";
    public static final String DEFAULT_INVOICE_GENERATED_NOTIFICATION_EMAIL_SUBJ = "Invoice Generated Notification";
    public static final String DEFAULT_INVOICE_EXPIRATION_REMINDER_EMAIL_SUBJ = "Invoice Expiration Reminder";
    public static final String DEFAULT_CLIENT_REVIEW_NOTIFICATION_EMAIL_SUBJ = "Engineer Rating and Review Reminder";
    public static final String DEFAULT_NOTIFY_ADMIN_CLIENT_REVIEW_EMAIL_SUBJ = "Engineer Review Notification";
    public static final String DEFAULT_WEEKLY_PERFORMANCE_EMAIL_SUBJ = "Weekly Dev Performance";
    public static final String DEFAULT_ENQUIRY_NOTIFICATION_EMAIL_SUBJ = "Enquiry Notification";
    public static final String DEFAULT_ORGANIZATION_USER_SIGN_UP_SUBJ = "Hire the best software engineering talent";
    public static  final String DEFAULT_ENGINEER_PLACEMENT_REQUEST_NOTIFICATION_EMAIL_SUBJ = "New Engineer Request on Decagon Client Portal";

    //db configuration constant up to 30 characters in length
    public static final String SUPER_ADMINISTRATOR_EMAIL = "SUPER_ADMIN_EMAIL";
    public static final String SUPER_ADMINISTRATOR_DEFAULT_PASS = "SUPER_ADMIN_DEFAULT_PASS";
    public static final String APP_NAME = "APP_NAME";
    public static final String INVOICE_COMPANY_NAME = "INVOICE_COMPANY_NAME";
    public static final String INVOICE_COMPANY_ADDRESS = "INVOICE_COMPANY_ADDRESS";
    public static final String EMAIL_CONFIG = "EMAIL_CONFIG";
    public static final String COMPANY_ACCOUNT_DETAILS = "COMPANY_ACCOUNT_DETAILS";
    public static final String PSSWORD_SETTING_TOKEN_VLDTY_TRM = "PSSWORD_SETTING_TOKEN_VLDTY_TRM";
    public static final String HEAR_ABT_US = "HEAR_ABT_US";
    public static final String COMPANY_NAME = "COMPANY_NAME";
    public static final String COMPANY_EMAIL = "COMPANY_EMAIL";
    public static final String EMAILS = "EMAILS";
    public static final String FILE_CLAIM_REQUEST_TO_EMAIL = "FILE_CLAIM_REQ_EMAIL";
    public static final String ENGINEER_ASSIGNMENT_REQUEST_TO_EMAIL = "ENGR_ASSIGNMNT_REQ_EMAIL";
    public static final String CANCEL_ENGINEER_REQUEST_LINE_ITEM_TO_EMAIL = "CANCEL_ENGINEER_REQUEST_LINE_ITEM_EMAIL";
    public static final String EMAIL_SUBJ_CREATE_PSSWORD_EMAIL = "CREATE_PSSWORD_MAIL_SUBJ";
    public static final String EMAIL_SUBJ_PASSWORD_CREATED_MAIL = "PASSWORD_CREATED_MAIL_SUBJ";
    public static final String EMAIL_SUBJ_CREATE_ADMIN_USER_EMAIL = "CREATE_ADMIN_USER_EMAIL_SUBJ";
    public static final String EMAIL_SUBJ_CREATE_AFFILIATE_USER_EMAIL = "CREATE_AFFILIATE_USER_EMAIL_SUBJ";
    public static final String EMAIL_SUBJ_NOTIFY_ADMIN_USER_NEW_AFFILIATE_USER_EMAIL = "EMAIL_SUBJ_NOTIFY_ADMIN_USER_NEW_AFFILIATE_USER_EMAIL_SUBJ";
    public static final String EMAIL_SUBJ_AFFILIATE_USER_APPROVAL_EMAIL = "EMAIL_SUBJ_AFFILIATE_USER_APPROVAL_EMAIL";
    public static final String EMAIL_SUBJ_NOTIFICATION_EMAIL = "EMAIL_SUBJ_NOTIFICATION_EMAIL";
    public static final String EMAIL_SUBJ_CREATE_CUSTOMER_USER_EMAIL = "CREATE_CUSTOMER_USER_EMAIL_SUBJ";
    public static final String EMAIL_SUBJ_CREATE_RIDER_LOGIN_OTP_EMAIL = "EMAIL_SUBJ_CREATE_RIDER_LOGIN_OTP_EMAIL";
    public static final String EMAIL_SUBJ_SUBSCRIPTION_REQUEST_EMAIL = "SUBSCRIPTION_EMAIL_SUBJ";
    public static final String EMAIL_SUBJ_ENGINEER_ASSIGNMENT_REQUEST_EMAIL = "ENGR_ASSIGNMENT_REQ_EMAIL_SUBJ";
    public static final String EMAIL_SUBJ_CANCEL_ENGINEER_REQUEST_LINE_ITEM_EMAIL = "CANCEL_ENGR_REQ_LINE_ITEM EMAIL_SUBJ";
    public static final String SUPPORT_EMAIL = "ADMIN_EMAIL";
    public static final String CHECK_SUBSRIPTION_ON_ASSIGN_ENGINEER = "CHECK_SUBSR_ON_PLACEMENT";
    public static final String EMAIL_SUBJ_SUBSCRIPTION_EXPIRATION_REMINDER_EMAIL = "SUBSCRIPTION_EXPIRATION_REMINDER_EMAIL_SUBJ";
    public static final String EMAIL_SUBJ_INVOICE_GENERATED_NOTIFICATION_EMAIL = "INVOICE_GENERATED_NOTIFICATION_EMAIL_SUBJ";
    public static final String EMAIL_SUBJ_INVOICE_EXPIRATION_REMINDER_EMAIL = "INVOICE_EXPIRATION_REMINDER_EMAIL";
    public static final String EMAIL_SUBJ_CLIENT_REVIEW_NOTIFICATION_EMAIL = "CLIENT_REVIEW_NOTIFICATION_EMAIL";
    public static final String EMAIL_SUBJ_NOTIFY_ADMIN_CLIENT_REVIEW_EMAIL = "NOTIFY_ADMIN_CLIENT_REVIEW_EMAIL";
    public static final String EMAIL_SUBJ_WEEKLY_PERFORMANCE_EMAIL = "WEEKLY_PERFORMANCE_EMAIL";
    public static final String EMAIL_SUBJ_ENQUIRY_NOTIFICATION_EMAIL = "EMAIL_SUBJ_ENQUIRY_NOTIFICATION_EMAIL";
    public static final String ENQUIRY_TO_EMAIL = "ENQUIRY_TO_EMAIL";
    public static final String EMAIL_SUBJ_ENGINEER_PLACEMENT_REQUEST_NOTIFICATION = "EMAIL_SUBJ_ENGINEER_PLACEMENT_REQUEST_NOTIFICATION";
    public static final String EMAIL_SUBJ_ORGANIZATION_USER_SIGN_UP = "HIRE_THE_SOFTWARE_ENGINEERING_TALENT";
    public static final int FIRST_NAME_COL_SIZE = 100;
    public static final int LAST_NAME_COL_SIZE = 100;
    public static final int EMAIL_COL_SIZE = 100;
    public static final int PHONE_NO_COL_SIZE = 100;
    public static final int TRAINING_DATA_POINT_TITLE_COL_SIZE = 100;
    public static final int GRADE_SCALE_TITLE_COL_SIZE = 100;
    public static final int WEEK_TITLE_COL_SIZE = 100;
    public static final int TRAINING_DATA_POINT_CODE_COL_SIZE = 3;
    public static final int ABOUT_COL_SIZE = 255;
    public static final int TECHNICAL_SKILLS_COL_SIZE = 255;
    public static final int RESUME_LINK_COL_SIZE = 255;
    public static final int GITHUB_PROFILE_LINK_COL_SIZE = 255;
    public static final int LINKEDLN_PROFILE_LINK_COL_SIZE = 255;
    public static final int WEEK_NAME_COL_SIZE = 100;
    public static final int SQUAD_NAME_COL_SIZE = 100;
    public static final int COMMENT_COL_SIZE = 255;
    public static final int VIDEO_LINK_COL_SIZE = 255;
    public static final int ORGANIZATION_ROLE_COL_SIZE = 100;
    public static final int RESUME_VIDEO_LINK_COL_SIZE = 255;

    /**
     * Country iso codes
     * country codes commented out do not have valid locale
     */
    public static String[] getCountryIsoCode() {

        return new String[]{"AE", "AF", "AX", "AL", "DZ",
               /*"AN",*/"AS", "AD", "AO", "AI", /*"AQ",*/ "AG", "AR", "AM", "AW", "AU", "AT", "AZ", "BS", "BH",
               "BD", "BB", "BY", "BE", "BZ", "BJ", "BM", "BT", "BO", "BA", "BW", /*"BV",*/ "BR", "IO",
               "BN", "BG", "BF", "BI", "KH", "CM", "CA", "CV", "KY", "CF", "CL", "CN", "CX",
               "CC", "CO", "KM", "CG", "CD", "CK", "CR", "CI", "HR", "CU", "CY", "CZ", "DK", "DJ",
               "DM", "DO", "EC", "EG", "SV", "GQ", "ER", "EE", "ET", "FK", "FO", "FJ", "FI", "FR",
               "GF", "PF", "GA", "GM", "GE", "DE", "GH", "GI", "GR", "GL", "GD", "GP", "GU",/*"GS",*/
               "GT", "GG", "GN", "GW", "GY", "HT", /*"HM",*/ "VA", "HN", "HK", "HU", "IS", "IN", "ID",
               "IR", "IQ", "IE", "IM", "IL", "IT", "JM", "JP", "JE", "JO", "KZ", "KE", "KI", "KP",
               "KR", "KW", "KG", "LA", "LV", "LB", "LS", "LR", "LY", "LI", "LT", "LU", "MO", "MK",
               "MG", "MW", "MY", /*"MV",*/ "ML", "MT", "MH", "MQ", "MR", "MU", "YT", "MX", "FM", "MD",
               "MC", "MN", "ME", "MS", "MA", "MZ", "MM", "NA", "NR", "NP", "NL", "NC", "NZ",
               "NI", "NE", "NG", "NU", "NF", "MP", "NO", "OM", "PK", "PW", "PS", "PA", "PG", "PY",
               "PE", "PH", "PN", "PL", "PT", "PR", "QA", "RE", "RO", "RU", "RW", "SH", "KN", "LC",
               "PM", "VC", "WS", "SM", "ST", "SA", "SN", "RS", "SC", "SL", "SG", "SK", "SI", "SB",
               "SO", "ZA", "ES", "LK", "SD", "SR", "SJ", "SZ", "SE", "CH", "SY", "TW", "TD", "TJ",
               /*"TF",*/"TZ", "TH", "TL", "TG", "TK", "TO", "TT", "TN", "TR", "TM", "TC", "TV", "UG", "UA",
               "GB", "US", "UM", "UY", "UZ", "VU", "VE", "VN", "VG", "VI", "WF", "EH",
               "YE", "ZM", "ZW"};
    }
    /**
     * Locale per country iso codes
     */
    public static HashMap<String, Locale> getLocales(){

       HashMap<String, Locale> LOCALES = new HashMap<String, Locale>();
        //static {
            for (Locale locale : Locale.getAvailableLocales()) {
                LOCALES.put(locale.getCountry(), locale);
            }
        //}
        return LOCALES;
    }



    /**
     * Currency codes with name
     */
    public static HashMap<String, String> getCurrenciesMap() {

        HashMap<String, String> CURRENCY_MAP = new HashMap<String, String>();

            CURRENCY_MAP.put("AFN", "Afghani");
            CURRENCY_MAP.put("ALL", "Lek");
            CURRENCY_MAP.put("DZD", "Algerian Dinar");
            CURRENCY_MAP.put("AOA", "Kwanza");
            CURRENCY_MAP.put("ARS", "Argentine Peso");
            CURRENCY_MAP.put("AMD", "Armenian Dram");
            CURRENCY_MAP.put("AWG", "Aruban Florin");
            CURRENCY_MAP.put("AZN", "Azerbaijanian Manat");
            CURRENCY_MAP.put("BSD", "Bahamian Dollar");
            CURRENCY_MAP.put("BHD", "Bahraini Dinar");
            CURRENCY_MAP.put("BDT", "Taka");
            CURRENCY_MAP.put("BBD", "Barbados Dollar");
            CURRENCY_MAP.put("BYR", "Belarussian Ruble");
            CURRENCY_MAP.put("BZD", "Belize Dollar");
            CURRENCY_MAP.put("BMD", "Bermudian Dollar");
            CURRENCY_MAP.put("BTN", "Ngultrum");
            CURRENCY_MAP.put("BOB", "Boliviano");
            CURRENCY_MAP.put("BOV", "Mvdol");
            CURRENCY_MAP.put("BAM", "Convertible Mark");
            CURRENCY_MAP.put("BWP", "Pula");
            CURRENCY_MAP.put("BRL", "Brazilian Real");
            CURRENCY_MAP.put("BND", "Brunei Dollar");
            CURRENCY_MAP.put("BGN", "Bulgarian Lev");
            CURRENCY_MAP.put("BIF", "Burundi Franc");
            CURRENCY_MAP.put("KHR", "Riel");
            CURRENCY_MAP.put("CAD", "Canadian Dollar");
            CURRENCY_MAP.put("CVE", "Cape Verde Escudo");
            CURRENCY_MAP.put("KYD", "Cayman Islands Dollar");
            CURRENCY_MAP.put("CLF", "Unidades de fomento");
            CURRENCY_MAP.put("CLP", "Chilean Peso");
            CURRENCY_MAP.put("CNY", "Yuan Renminbi");
            CURRENCY_MAP.put("COP", "Colombian Peso");
            //CURRENCY_MAP.put("COU", "Unidad de Valor Real");
            CURRENCY_MAP.put("KMF", "Comoro Franc");
            CURRENCY_MAP.put("CDF", "Congolese Franc");
            CURRENCY_MAP.put("CRC", "Costa Rican Colon");
            CURRENCY_MAP.put("HRK", "Croatian Kuna");
            //CURRENCY_MAP.put("CUC", "Peso Convertible");
            CURRENCY_MAP.put("CUP", "Cuban Peso");
            CURRENCY_MAP.put("CZK", "Czech Koruna");
            CURRENCY_MAP.put("DJF", "Djibouti Franc");
            CURRENCY_MAP.put("DOP", "Dominican Peso");
            CURRENCY_MAP.put("EGP", "Egyptian Pound");
            CURRENCY_MAP.put("SVC", "El Salvador Colon");
            CURRENCY_MAP.put("ERN", "Nakfa");
            CURRENCY_MAP.put("ETB", "Ethiopian Birr");
            CURRENCY_MAP.put("FKP", "Falkland Islands Pound");
            CURRENCY_MAP.put("FJD", "Fiji Dollar");
            CURRENCY_MAP.put("XAF", "CFA Franc BEAC");
            CURRENCY_MAP.put("GMD", "Dalasi");
            CURRENCY_MAP.put("GEL", "Lari");
            CURRENCY_MAP.put("GHS", "Ghana Cedi");
            CURRENCY_MAP.put("GIP", "Gibraltar Pound");
            CURRENCY_MAP.put("DKK", "Danish Krone");
            CURRENCY_MAP.put("GTQ", "Quetzal");
            CURRENCY_MAP.put("GNF", "Guinea Franc");
            CURRENCY_MAP.put("GYD", "Guyana Dollar");
            CURRENCY_MAP.put("HTG", "Gourde");
            CURRENCY_MAP.put("HNL", "Lempira");
            CURRENCY_MAP.put("HKD", "Hong Kong Dollar");
            CURRENCY_MAP.put("HUF", "Forint");
            CURRENCY_MAP.put("ISK", "Iceland Krona");
            CURRENCY_MAP.put("INR", "Indian Rupee");
            CURRENCY_MAP.put("IDR", "Rupiah");
            CURRENCY_MAP.put("XDR", "SDR (Special Drawing Right)");
            CURRENCY_MAP.put("IRR", "Iranian Rial");
            CURRENCY_MAP.put("IQD", "Iraqi Dinar");
            CURRENCY_MAP.put("ILS", "New Israeli Sheqel");
            CURRENCY_MAP.put("JMD", "Jamaican Dollar");
            CURRENCY_MAP.put("JPY", "Yen");
            CURRENCY_MAP.put("JOD", "Jordanian Dinar");
            CURRENCY_MAP.put("KZT", "Tenge");
            CURRENCY_MAP.put("KES", "Kenyan Shilling");
            CURRENCY_MAP.put("KPW", "North Korean Won");
            CURRENCY_MAP.put("KRW", "Won");
            CURRENCY_MAP.put("KWD", "Kuwaiti Dinar");
            CURRENCY_MAP.put("KGS", "Som");
            CURRENCY_MAP.put("LAK", "Kip");
            CURRENCY_MAP.put("LVL", "Latvian Lats");
            CURRENCY_MAP.put("LBP", "Lebanese Pound");
            CURRENCY_MAP.put("LSL", "Loti");
            CURRENCY_MAP.put("ZAR", "Rand");
            CURRENCY_MAP.put("LRD", "Liberian Dollar");
            CURRENCY_MAP.put("LYD", "Libyan Dinar");
            CURRENCY_MAP.put("CHF", "Swiss Franc");
            CURRENCY_MAP.put("LTL", "Lithuanian Litas");
            CURRENCY_MAP.put("EUR", "Euro");
            CURRENCY_MAP.put("MOP", "Pataca");
            CURRENCY_MAP.put("MKD", "Denar");
            CURRENCY_MAP.put("MGA", "Malagasy Ariary");
            CURRENCY_MAP.put("MWK", "Kwacha");
            CURRENCY_MAP.put("MYR", "Malaysian Ringgit");
            CURRENCY_MAP.put("MVR", "Rufiyaa");
            CURRENCY_MAP.put("MRO", "Ouguiya");
            CURRENCY_MAP.put("MUR", "Mauritius Rupee");
            //CURRENCY_MAP.put("XUA", "ADB Unit of Account");
            CURRENCY_MAP.put("MXN", "Mexican Peso");
            CURRENCY_MAP.put("MXV", "Mexican Unidad de Inversion (UDI)");
            CURRENCY_MAP.put("MDL", "Moldovan Leu");
            CURRENCY_MAP.put("MNT", "Tugrik");
            CURRENCY_MAP.put("MZN", "Mozambique Metical");
            CURRENCY_MAP.put("MMK", "Kyat");
            CURRENCY_MAP.put("NAD", "Namibia Dollar");
            CURRENCY_MAP.put("NPR", "Nepalese Rupee");
            CURRENCY_MAP.put("NIO", "Cordoba Oro");
            CURRENCY_MAP.put("NGN", "Naira");
            CURRENCY_MAP.put("OMR", "Rial Omani");
            CURRENCY_MAP.put("PKR", "Pakistan Rupee");
            CURRENCY_MAP.put("PAB", "Balboa");
            CURRENCY_MAP.put("PGK", "Kina");
            CURRENCY_MAP.put("PYG", "Guarani");
            CURRENCY_MAP.put("PEN", "Nuevo Sol");
            CURRENCY_MAP.put("PHP", "Philippine Peso");
            CURRENCY_MAP.put("PLN", "Zloty");
            CURRENCY_MAP.put("QAR", "Qatari Rial");
            CURRENCY_MAP.put("RON", "New Romanian Leu");
            CURRENCY_MAP.put("RUB", "Russian Ruble");
            CURRENCY_MAP.put("RWF", "Rwanda Franc");
            CURRENCY_MAP.put("SHP", "Saint Helena Pound");
            CURRENCY_MAP.put("XCD", "East Caribbean Dollar");
            CURRENCY_MAP.put("WST", "Tala");
            CURRENCY_MAP.put("STD", "Dobra");
            CURRENCY_MAP.put("SAR", "Saudi Riyal");
            CURRENCY_MAP.put("RSD", "Serbian Dinar");
            CURRENCY_MAP.put("SCR", "Seychelles Rupee");
            CURRENCY_MAP.put("SLL", "Leone");
            CURRENCY_MAP.put("SGD", "Singapore Dollar");
            //CURRENCY_MAP.put("XSU", "Sucre");
            CURRENCY_MAP.put("SBD", "Solomon Islands Dollar");
            CURRENCY_MAP.put("SOS", "Somali Shilling");
            //CURRENCY_MAP.put("SSP", "South Sudanese Pound");
            CURRENCY_MAP.put("LKR", "Sri Lanka Rupee");
            CURRENCY_MAP.put("SDG", "Sudanese Pound");
            CURRENCY_MAP.put("SRD", "Surinam Dollar");
            CURRENCY_MAP.put("NOK", "Norwegian Krone");
            CURRENCY_MAP.put("SZL", "Lilangeni");
            CURRENCY_MAP.put("SEK", "Swedish Krona");
            //CURRENCY_MAP.put("CHE", "WIR Euro");
            //CURRENCY_MAP.put("CHW", "WIR Franc");
            CURRENCY_MAP.put("SYP", "Syrian Pound");
            CURRENCY_MAP.put("TWD", "New Taiwan Dollar");
            CURRENCY_MAP.put("TJS", "Somoni");
            CURRENCY_MAP.put("TZS", "Tanzanian Shilling");
            CURRENCY_MAP.put("THB", "Baht");
            CURRENCY_MAP.put("XOF", "CFA Franc BCEAO");
            CURRENCY_MAP.put("NZD", "New Zealand Dollar");
            CURRENCY_MAP.put("TTD", "Trinidad and Tobago Dollar");
            CURRENCY_MAP.put("TND", "Tunisian Dinar");
            CURRENCY_MAP.put("TRY", "Turkish Lira");
            //CURRENCY_MAP.put("TMT", "Turkmenistan New Manat");
            CURRENCY_MAP.put("AUD", "Australian Dollar");
            CURRENCY_MAP.put("UGX", "Uganda Shilling");
            CURRENCY_MAP.put("UAH", "Hryvnia");
            CURRENCY_MAP.put("AED", "UAE Dirham");
            CURRENCY_MAP.put("GBP", "Pound Sterling");
            //CURRENCY_MAP.put("UYI", "Uruguay Peso en Unidades Indexadas (URUIURUI)");
            CURRENCY_MAP.put("UYU", "Peso Uruguayo");
            CURRENCY_MAP.put("UZS", "Uzbekistan Sum");
            CURRENCY_MAP.put("VUV", "Vatu");
            CURRENCY_MAP.put("VEF", "Bolivar Fuerte");
            CURRENCY_MAP.put("VND", "Dong");
            CURRENCY_MAP.put("USD", "US Dollar");
            CURRENCY_MAP.put("XPF", "CFP Franc");
            CURRENCY_MAP.put("MAD", "Moroccan Dirham");
            CURRENCY_MAP.put("YER", "Yemeni Rial");
            CURRENCY_MAP.put("ZMK", "Zambian Kwacha");
            //CURRENCY_MAP.put("ZWL", "Zimbabwe Dollar");
        return CURRENCY_MAP;

    }

    public static final String TABLE_ADMIN_LOGIN_HISTORY="ADMIN_LOGIN_HISTORY";
    public static final String TABLE_ADMIN_USER="ADMIN_USER";
    public static final String TABLE_ACCESS_CODE="ACCESS_CODE";
//    public static final String TABLE_CUSTOMER_CSE_MAP ="CUSTOMER_CSE_MAP";
    public static final String TABLE_USER_ROLE="USER_ROLE";
    public static final String TABLE_CUSTOMER_CSE_MAP="CUSTOMER_CSE_MAP";
    public static final String TABLE_CLIENT_LOGIN_HISTORY="CLIENT_LOGIN_HISTORY";
    public static final String TABLE_COUNTRY_CODE="COUNTRY";
    public static final String TABLE_LOCALE_STATES="LOCALE_STATES";
    public static final String TABLE_LOCALE_COUNTRIES="LOCALE_COUNTRIES";
    public static final String TABLE_LOCALE_CITIES="LOCALE_CITIES";
    public static final String TABLE_STATE="STATE";
    public static final String TABLE_INTERNATIONAL_COUNTRY ="INTERNATIONAL_COUNTRY";
    public static final String TABLE_INTERNATIONAL_REGIONS ="INTERNATIONAL_REGIONS";
    public static final String TABLE_CUSTOMER_REFRESH_TOKEN="User_REFRESH_TOKEN";
    public static final String TABLE_AFFILIATES="AFFILIATES";
    public static final String TABLE_RIDERS="RIDER_USER";
    public static final String TABLE_RIDER_SHIPMENT_MAP="RIDER_SHIPMENT_MAP";
    public static final String TABLE_CUSTOMER_USERS="CUSTOMER_USERS";
    public static final String TABLE_USERS="USERS";
    public static final String TABLE_SHIPMENT ="SHIPMENT";
    public static final String TABLE_WALLET ="WALLET";
    public static final String TABLE_TRANSACTION ="TRANSACTION";
    public static final String TABLE_SHIPMENT_PROFILE ="SHIPMENT_PROFILE";
    public static final String TABLE_FILE_CLAIMS ="FILE_CLAIMS";
    public static final String TABLE_SERVICE_OPTION ="SERVICE_OPTION";
    public static final String TABLE_WITHDRAWAL ="WITHDRAWAL";
    public static final String TABLE_COMMISSION_RATE = "commission_rates";
    public static final String TABLE_PRODUCT_ITEM ="PRODUCT_ITEM";
    public static final String TABLE_CUSTOMER_SHIPMENT_MAP ="CUSTOMER_SHIPMENT_MAP";
    public static final String TABLE_PACKAGE_CATEGORY = "PACKAGE_CATEGORY";
    public static final String TABLE_PRICING_LEVELS = "PRICING_LEVELS";
    public static final String TABLE_LOCAL_PICKUP_LOCATION = "LOCAL_PICKUP_LOCATION";
    public static final String TABLE_PICKUP_STATE = "PICKUP_STATE";
    public static final String TABLE_INTERNATIONAL_SHIPPING_PRICE = "INTERNATION_SHIPPING_PRICE";
    public static final String TABLE_NATIONAL_WIDE_REGION = "NATION_WIDE_REGION";
    public static final String TABLE_NATIONAL_WIDE_STATE = "NATION_WIDE_STATE";
    public static final String TABLE_NATIONAL_WIDE_CITY = "NATION_WIDE_CITY";
    public static final String TABLE_SHIPPING_SERVICE = "SHIPPING_SERVICE";

    public static final String TABLE_PERMISSIONS="PERMISSIONS";
    public static final String TABLE_ROLES="ROLES";
    public static final String TABLE_ROLE_PERMISSIONS="ROLES_PERMISSIONS";
    public static final String TABLE_SENDER_DETAILS="SHIPMENT_SENDER";
    public static final String TABLE_RECEIVER_DETAILS="SHIPMENT_RECEIVER";
    public static final String TABLE_AFFILIATE_BENEFITED_RECORDS="AFFILIATE_BENEFITED_RECORDS";

    public static final String TABLE_SUBSCRIPTION_REQUESTS="SUBSCRIPTION_REQUESTS";
    public static final String TABLE_SYSTEM_CONFIGURATION="SYSTEM_CONFIGURATION";
    public static final String TABLE_EXPECTED_DELIVERY_DAY_INTERNATION ="INTERNATION_EXPECTED_DELIVERY_DAY";
    public static final String TABLE_TECH_STACKS="TECH_STACKS";
    public static final String TABLE_TOKEN_BLACKLIST="TOKEN_BLACKLIST";
    public static final String TABLE_INVOICE="INVOICE";
    public static final String TABLE_DOCUMENT="DOCUMENT";
    public static final String TABLE_QUOTE="QUOTE";
    public static final String TABLE_API_SESSION="API_SESSIONS";
    public static final String TABLE_NOTIFICATION="NOTIFICATION";
    public static final String TABLE_MAILINGLIST="MAILINGLIST";
    public static final String TABLE_EMAILSUBSCRIPTION="EMAILSUBSCRIPTION";



    //schema const
    public static final int PROJECT_NAME_COL_SIZE = 255;
    public static final int REVIEW_COL_SIZE = 3000;
    public static final int REQUEST_ENGINEER_COMMENT_COL_SIZE = 3000;
    public static final int PROJECT_DESC_COL_SIZE = 1000;
    public static final int TECHNOLOGY_USED_COL_SIZE = 255;
    public static final int PROJECT_LINK_COL_SIZE = 255;
    public static final int INSTITUTION_COL_SIZE = 255;
    public static final int WORK_EXPERIENCE_DESC_COL_SIZE = 1000;
    public static final int COURSE_COL_SIZE = 255;
    public static final int GRADE_COL_SIZE = 255;
    public static final int QUALIFICATION_COL_SIZE = 255;
    public static final int WORK_EXPERIENCE_COMPANY_NAME_COL_SIZE = 255;
    public static final int WORK_EXPERIENCE_ROL_COL_SIZE = 255;
    public static final int NICK_NAME_COL_SIZE = 100;


}




