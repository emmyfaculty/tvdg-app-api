package com.tvdgapp.utils;


import com.tvdgapp.exceptions.UnAuthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserInfoUtil {

    public UserDetails authenticatedUserInfo() {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null) {
            Authentication auth = securityContext.getAuthentication();
            if (auth != null) {
                return (UserDetails) auth.getPrincipal();
            }
        }
        throw new UnAuthorizedException("Authenticated user info not found");
    }


//    public MyUserDetails authenticatedCustomerUserInfo() {
//        UserDetails userDetails = this.authenticatedUserInfo();
//        if (userDetails instanceof MyUserDetails) {
//            return (MyUserDetails) userDetails;
//        }
//        throw new UnAuthorizedException("Authenticated user info not found");
//    }


//    public SecuredAffiliateUserInfo authenticatedAffiliateUserInfo() {
//        UserDetails userDetails = this.authenticatedUserInfo();
//        if (userDetails instanceof SecuredAffiliateUserInfo) {
//            return (SecuredAffiliateUserInfo) userDetails;
//        }
//        throw new UnAuthorizedException("Authenticated user info not found");
//    }

//    public SecuredAdminUserInfo authenticatedAdminUserInfo() {
//        UserDetails userDetails = this.authenticatedUserInfo();
//        if (userDetails instanceof SecuredAdminUserInfo) {
//            return (SecuredAdminUserInfo) userDetails;
//        }
//        throw new UnAuthorizedException("Authenticated user info not found");
//    }

//    public User authenticatedUserInfo() {
//        UserDetails userDetails = getAuthenticatedUserDetails();
//
//        if (userDetails instanceof User) {
//            return (User) userDetails;
//        }
//
//        throw new UnAuthorizedException("Authenticated user info not found");
//    }

    public UserDetails AuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnAuthorizedException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        } else {
            throw new UnAuthorizedException("Authenticated user details not found");
        }
    }

//    public SecuredCustomerUserInfo authenticatedEngineerUserInfo() {
//        UserDetails userDetails = this.authenticatedUserInfo();
//        if (userDetails instanceof SecuredCustomerUserInfo) {
//            return (SecuredCustomerUserInfo) userDetails;
//        }
//        throw new UnAuthorizedException("Authenticated user info not found");
//    }

//    public boolean isAdmin() {
//        return this.authenticatedUserInfo().getClass().equals(SecuredAdminUserInfo.class);
//    }
}
