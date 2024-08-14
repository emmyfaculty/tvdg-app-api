package com.tvdgapp.securityconfig;

import com.tvdgapp.models.user.User;
import com.tvdgapp.models.user.UserStatus;
import com.tvdgapp.models.user.admin.AdminUser;
import com.tvdgapp.utils.AuthUtils;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Setter
@Getter
@SuppressWarnings("NullAway.Init")
public class SecuredUserInfo implements UserDetails {

    protected User user;

    protected  Collection<? extends GrantedAuthority> authorities;


    public SecuredUserInfo(User user) {
        this.user = user;
        this.authorities= AuthUtils.getUserAuthorities(this.user.getRoles());
    }
    public Long getUserId() {
        return user.getId();
    }

    public User getUser(){
        return user;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus().equals(UserStatus.ACTIVE);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName=" + this.getUsername() +
                ",isEnabled=" + this.isEnabled() +
                "user=" + user +
                '}';
    }


}
