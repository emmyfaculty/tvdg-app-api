package com.tvdgapp.securityconfig;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomBCryptPasswordEncoder implements PasswordEncoder {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomBCryptPasswordEncoder() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
        return encodedPassword.replaceFirst("\\$2a\\$", "\\$2y\\$");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String updatedPassword = encodedPassword.replaceFirst("\\$2y\\$", "\\$2a\\$");
        return bCryptPasswordEncoder.matches(rawPassword, updatedPassword);
    }
}
