//package com.tvdgapp.services;
//
//import com.tvdgapp.models.user.customer.CustomerUser;
//import com.tvdgapp.repositories.User.CustomerUserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//@RequiredArgsConstructor
//@Component("customerUserDetailsService")
//public class CustomerUserDetailsService implements UserDetailsService {
//
//    private final CustomerUserRepository userRepository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        CustomerUser user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("No user found with username:" + username));
//        return new MyUserDetails(user);
//    }
//}
