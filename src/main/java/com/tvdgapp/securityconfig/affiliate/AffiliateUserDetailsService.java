//package com.tvdgapp.securityconfig.affiliate;
//
//
//import com.tvdgapp.models.user.affiliateuser.AffiliateUser;
//import com.tvdgapp.repositories.affiliate.AffiliateRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//
///**
// *
// */
//@RequiredArgsConstructor
//@Component("affiliateUserDetailsService")
//public class AffiliateUserDetailsService implements UserDetailsService {
//
//    private final AffiliateRepository userRepository;
//
//    @Override
////    @Transactional
////    @Cacheable("affiliateUserDetailsService")//todo:should not cache due to security reasons
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        AffiliateUser user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("No user found with username:" + username));
//        return new SecuredAffiliateUserInfo(user);
//    }
//
//
//}
