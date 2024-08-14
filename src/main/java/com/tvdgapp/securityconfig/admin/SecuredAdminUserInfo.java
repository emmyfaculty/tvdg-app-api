//package com.tvdgapp.securityconfig.admin;
//
//
//import com.tvdgapp.models.user.admin.AdminUser;
//import com.tvdgapp.securityconfig.SecuredUserInfo;
//import com.tvdgapp.utils.AuthUtils;
//
//public class SecuredAdminUserInfo extends SecuredUserInfo {
//
//    public SecuredAdminUserInfo(AdminUser user) {
//        this.user = user;
//        this.authorities= AuthUtils.getUserAuthorities(((AdminUser)this.user).getRoles());
//    }
//    public Long getUserId() {
//        return user.getId();
//    }
//
//    public AdminUser getAdminUser(){
//        return (AdminUser) user;
//    }
//}