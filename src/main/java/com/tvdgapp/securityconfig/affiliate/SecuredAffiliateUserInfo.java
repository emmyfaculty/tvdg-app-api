//package com.tvdgapp.securityconfig.affiliate;
//
//
//import com.tvdgapp.models.user.affiliateuser.AffiliateUser;
//import com.tvdgapp.securityconfig.SecuredUserInfo;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//@Getter
//@Setter
//@RequiredArgsConstructor
////public class SecuredAffiliateUserInfo implements UserDetails {
//public class SecuredAffiliateUserInfo extends SecuredUserInfo {
//
////    private final AffiliateUser user;
//    public SecuredAffiliateUserInfo(AffiliateUser user) {
//        this.user = user;
//        this.authorities = Collections.emptyList();
//    }
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//
//        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRoleType().name()));
//
//    }
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//    @Override
//    public String getUsername() {
//        return user.getEmail();
//    }
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }


//}