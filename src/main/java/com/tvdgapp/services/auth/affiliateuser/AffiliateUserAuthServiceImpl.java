//package com.tvdgapp.services.auth.affiliateuser;
//
//import com.tvdgapp.dtos.auth.LoginResponseDto;
//import com.tvdgapp.exceptions.AccountDisabledException;
//import com.tvdgapp.exceptions.InvalidCredentialsException;
//import com.tvdgapp.exceptions.TvdgException;
//import com.tvdgapp.models.user.affiliateuser.AffiliateUser;
//import com.tvdgapp.models.user.User;
//import com.tvdgapp.models.user.UserType;
//import com.tvdgapp.repositories.affiliate.AffiliateRepository;
//import com.tvdgapp.securityconfig.JwtTokenProvider;
//import com.tvdgapp.services.AuthService;
//import com.tvdgapp.utils.AuthUtils;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//@Service("affiliateAuthService")
//@RequiredArgsConstructor
//public class AffiliateUserAuthServiceImpl implements AuthService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AffiliateUserAuthServiceImpl.class);
//
//    private final JwtTokenProvider tokenProvider;
//    private final AffiliateRepository repository;
//    private final AuthenticationManager authenticationManager;
//
//    @Override
//    public LoginResponseDto login(String username, String password) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//            AffiliateUser user = repository.findByEmail(username).orElseThrow(() -> new InvalidCredentialsException("User with supplied credential does not exist"));
//            String token = tokenProvider.generateToken(authentication.getName(), UserType.AFFILIATE_USER.name());
//            if (isEmptyToken(token)) {
//                throw new TvdgException.UnAuthorizeException("invalid username/password:");
//            }
//            // Create response
//            LoginResponseDto authResponse = createLoginResponse(user, token);
//            authResponse.setAuthorities(AuthUtils.buildAuthorities(authentication));
//            return authResponse;
//
//        } catch (Exception e) {
//            if (e instanceof BadCredentialsException) {
//                throw new InvalidCredentialsException("invalid username/password:");
//            } else if (e instanceof DisabledException) {
//                throw new AccountDisabledException("Account disabled");
//            } else {
//                throw e;
//            }
//        }
//    }
//
//    private LoginResponseDto createLoginResponse(User user, String token) {
//        return AuthUtils.createLoginResponse(user, token);
//    }
//
//    private boolean isEmptyToken(String token) {
//        return AuthUtils.isEmptyToken(token);
//    }
//}
