package com.tvdgapp.securityconfig;


import com.tvdgapp.models.user.User;
import com.tvdgapp.models.user.UserStatus;
import com.tvdgapp.repositories.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


/**
 *
 */
@RequiredArgsConstructor
@Component("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user;
        if (login.contains("@")) {
            // Email login
            user = userRepository.findAuthUserByEmail(login)
                    .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + login));
        } else {
            // Phone number login
            user = userRepository.findAuthUserByTelephoneNumber(login)
                    .orElseThrow(() -> new UsernameNotFoundException("No user found with phone number: " + login));
        }

        // Additional logic to check user status and return CustomUserDetails
        if (user.getStatus() == UserStatus.PENDING || user.getStatus() != UserStatus.ACTIVE) {
            throw new DisabledException("User is pending or inactive");
        }

        return new SecuredUserInfo(user);
    }
}

