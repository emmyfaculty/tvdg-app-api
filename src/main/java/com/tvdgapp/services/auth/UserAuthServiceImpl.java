package com.tvdgapp.services.auth;

import com.tvdgapp.dtos.auth.LoginResponseDto;
import com.tvdgapp.dtos.auth.LogoutRequest;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.exceptions.*;
import com.tvdgapp.models.session.ApiSession;
import com.tvdgapp.models.session.DeviceType;
import com.tvdgapp.models.user.User;
import com.tvdgapp.models.user.UserRefreshToken;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.models.wallet.Wallet;
import com.tvdgapp.repositories.User.CustomerUserRepository;
import com.tvdgapp.repositories.User.UserRepository;
import com.tvdgapp.repositories.session.ApiSessionRepository;
import com.tvdgapp.repositories.wallet.WalletRepository;
import com.tvdgapp.securityconfig.JwtTokenProvider;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.services.AuthService;
import com.tvdgapp.utils.AuthUtils;
import com.tvdgapp.utils.CodeGeneratorUtils;
import com.tvdgapp.utils.EmailTemplateUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static com.tvdgapp.exceptions.EntityType.USER;

@RequiredArgsConstructor
@Service("userAuthService")
public class UserAuthServiceImpl implements AuthService {

//    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthServiceImpl.class);
    private static final Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);


    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final CustomerUserRepository customerUserRepository;
    private final WalletRepository walletRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenService refreshTokenService;
    private final EmailTemplateUtils emailTemplateUtils;
    private final ApiSessionRepository apiSessionRepository;

//    @Override
//    public LoginResponseDto login(String username, String password) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//            User user = userRepository.findByEmail(username).orElseThrow(() -> new InvalidCredentialsException("User with supplied credential does not exist"));
//            String token = tokenProvider.generateToken(authentication);
//            if (isEmptyToken(token)) {
//                throw new TvdgException.UnAuthorizeException("invalid username/password:");
//            }
//            UserRefreshToken refreshToken = this.refreshTokenService.createRefreshToken(user.getEmail());
//
//            // Check if the user is a customer
//            Optional<CustomerUser> customerUserOpt = customerUserRepository.findCustomerUserById(user.getId());
//            Optional<Wallet> walletOpt = Optional.ofNullable(walletRepository.findByUserId(user.getId()));
//
//            // Create response
//            LoginResponseDto authResponse = createLoginResponse(user, customerUserOpt.orElse(null), walletOpt.orElse(null), token);
//            authResponse.setAuthorities(AuthUtils.buildAuthorities(authentication));
//            authResponse.setRefreshToken(refreshToken.getToken());
//            return authResponse;
//
//        } catch (Exception e) {
//            if (e instanceof BadCredentialsException) {
//                throw new TvdgException.InvalidCredentialsException("invalid username/password:");
//            } else if (e instanceof DisabledException) {
//                throw new TvdgException.AccountDisabledException("Account disabled");
//            } else {
//                throw e;
//            }
//        }
//    }

//    public LoginResponseDto login(String username, String password) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//            User user = userRepository.findByEmail(username).orElseThrow(() -> new InvalidCredentialsException("User with supplied credential does not exist"));
//
//            // Check if the user is a rider and if OTP verification is required
//            if ("RIDER".equals(user.getRoles())) {
//                // Generate and send OTP
//                String otp = CodeGeneratorUtils.generateOTP();
//                user.setOtp(otp);
//                userRepository.save(user);
//                this.sendOTP(user, otp);
//
//                // Respond to the client to verify OTP
//                return new LoginResponseDto("OTP verification required. Please check your phone for the OTP.");
//            }
//
//            // Proceed with generating the token and returning the response if not a rider
//            String token = tokenProvider.generateToken(authentication);
//            if (isEmptyToken(token)) {
//                throw new TvdgException.UnAuthorizeException("Invalid username/password:");
//            }
//            UserRefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
//
//            // Update last login timestamp
//            user.setLastLogin(System.currentTimeMillis() / 1000L); // Unix timestamp
//            userRepository.save(user);
//
//            // Check if the user is a customer
//            Optional<CustomerUser> customerUserOpt = customerUserRepository.findCustomerUserById(user.getId());
//            Optional<Wallet> walletOpt = walletRepository.findByUserId(user.getId());
//
//            // Create response
//            LoginResponseDto authResponse = createLoginResponse(user, customerUserOpt.orElse(null), walletOpt.orElse(null),  token);
//            authResponse.setAuthorities(AuthUtils.buildAuthorities(authentication));
//            authResponse.setRefreshToken(refreshToken.getToken());
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



//    public LoginResponseDto login(String username, String password) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//            User user = userRepository.findByEmail(username).orElseThrow(() -> new InvalidCredentialsException("User with supplied credential does not exist"));
//
//            // Check if the user is a rider and if OTP verification is required
//            if ("RIDER".equals(user.getRoles())) {
//                // Generate and send OTP
//                String otp = CodeGeneratorUtils.generateOTP();
//                user.setOtp(otp);
//                userRepository.save(user);
//                this.sendOTP(user, otp);
//
//                // Respond to the client to verify OTP
//                return new LoginResponseDto("OTP verification required. Please check your phone for the OTP.");
//            }
//
//            // Proceed with generating the token and returning the response if not a rider
//            String token = tokenProvider.generateToken(authentication);
//            if (isEmptyToken(token)) {
//                throw new TvdgException.UnAuthorizeException("Invalid username/password:");
//            }
//            UserRefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
//
////     Update last login timestamp
//            user.setLastLogin(System.currentTimeMillis() / 1000L); // Unix timestamp
//            userRepository.save(user);
//
//            // Check if the user is a customer
//            Optional<CustomerUser> customerUserOpt = customerUserRepository.findCustomerUserById(user.getId());
//            Optional<Wallet> walletOpt = walletRepository.findByUserId(user.getId());
//
//            // Create response
//            LoginResponseDto authResponse = createLoginResponse(user, customerUserOpt.orElse(null), walletOpt.orElse(null),  token);
//            authResponse.setAuthorities(AuthUtils.buildAuthorities(authentication));
//            authResponse.setRefreshToken(refreshToken.getToken());
//            return authResponse;
//
//        } catch (Exception e) {
//            if (e instanceof BadCredentialsException) {
//                throw new TvdgException.InvalidCredentialsException("Invalid username/password:");
//            } else if (e instanceof DisabledException) {
//                throw new TvdgException.AccountDisabledException("Account disabled");
//            } else {
//                throw e;
//            }
//        }
//    }

//    @Override
//    @Transactional
//    public LoginResponseDto login(String login, String password) {
//        try {
//
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
//            User user = getUserByLogin(login);
//
//            // Ensure roles are initialized
//            user.getRoles().size();
//
//            // Logging to trace the roles
//            logger.debug("User roles: " + user.getRoles());
//
//            // Check if the user is a rider and if OTP verification is required
//            if (user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("RIDER"))) {
//                logger.debug("User has RIDER role");
//
//                // Generate and send OTP
//                String otp = CodeGeneratorUtils.generateOTP();
//                user.setOtp(otp);
//                userRepository.save(user);
//                sendOTP(user, otp);
//
//                // Respond to the client to verify OTP
//                return new LoginResponseDto("OTP verification required. Please check your phone for the OTP.");
//            }
//
//            logger.debug("User does not have RIDER role");
//
//            // Proceed with generating the token and returning the response if not a rider
//            String token = tokenProvider.generateToken(authentication);
//            if (isEmptyToken(token)) {
//                throw new TvdgException.UnAuthorizeException("Invalid username/password:");
//            }
//            UserRefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
//
//            // Update last login timestamp
//            user.setLastLogin(System.currentTimeMillis() / 1000L); // Unix timestamp
//            userRepository.save(user);
//
//            // Check if the user is a customer
//            Optional<CustomerUser> customerUserOpt = customerUserRepository.findCustomerUserById(user.getId());
//            Optional<Wallet> walletOpt = walletRepository.findByUserId(user.getId());
//
//            // Create response
//            LoginResponseDto authResponse = createLoginResponse(user, customerUserOpt.orElse(null), walletOpt.orElse(null), token);
//            authResponse.setAuthorities(AuthUtils.buildAuthorities(authentication));
//            authResponse.setRefreshToken(refreshToken.getToken());
//            return authResponse;
//
//        } catch (Exception e) {
//            if (e instanceof BadCredentialsException) {
//                throw new TvdgException.InvalidCredentialsException("Invalid username/password:");
//            } else if (e instanceof DisabledException) {
//                throw new TvdgException.AccountDisabledException("Account disabled");
//            } else {
//                throw e;
//            }
//        }
//    }

    @Override
    @Transactional
    public LoginResponseDto login(String login, String password, HttpServletRequest request) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, password)
            );

            // Retrieve the user by login
            User user = getUserByLogin(login);

            // Ensure roles are initialized
            user.getRoles().size();

            // Logging to trace the roles
            logger.debug("User roles: " + user.getRoles());

            // Check if OTP verification is enabled for the user
            if (user.isEnableOtp()) {
                logger.debug("OTP verification required");

                // Generate and send OTP
                String otp = CodeGeneratorUtils.generateOTP();
                user.setOtp(otp);
                userRepository.save(user);
                sendOTP(user, otp);

                // Respond to the client to verify OTP
                return new LoginResponseDto("OTP verification required. Please check your phone for the OTP.");
            }

            logger.debug("OTP verification not required");

            // Generate JWT token
            String token = tokenProvider.generateToken(authentication);
            if (isEmptyToken(token)) {
                throw new TvdgException.UnAuthorizeException("Invalid username/password:");
            }

            // Create a refresh token
            UserRefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

            // Update last login timestamp
            user.setLastLogin(System.currentTimeMillis() / 1000L); // Unix timestamp
            userRepository.save(user);

            // Extract additional session details
            String deviceId = request.getHeader("Device-Id");
            String deviceType = getDeviceType(request.getHeader("User-Agent"));
            String ipAddress = request.getRemoteAddr();
            String location = getLocationFromIp(ipAddress);
            String os = getOperatingSystem(request.getHeader("User-Agent"));
            String userAgent = request.getHeader("User-Agent");

            // Store session information
            ApiSession apiSession = new ApiSession();
            apiSession.setUserId(user.getId());
            apiSession.setToken(token);
            apiSession.setDeviceId(deviceId);
            apiSession.setDeviceType(DeviceType.valueOf(deviceType));
            apiSession.setIpAddress(ipAddress);
            apiSession.setLocation(location);
            apiSession.setOs(os);
            apiSession.setUserAgent(userAgent);
            apiSession.setCreatedAt(new Date());
            apiSession.setExpiresAt(new Date(System.currentTimeMillis() + 3600000)); // 1 hour session duration
            apiSession.setActive(true);
            apiSessionRepository.save(apiSession);

            // Check if the user is a customer
            Optional<CustomerUser> customerUserOpt = customerUserRepository.findCustomerUserById(user.getId());
            Optional<Wallet> walletOpt = walletRepository.findByUserId(user.getId());

            // Create the response object
            LoginResponseDto authResponse = createLoginResponse(
                    user,
                    customerUserOpt.orElse(null),
                    walletOpt.orElse(null),
                    token
            );
            authResponse.setAuthorities(AuthUtils.buildAuthorities(authentication));
            authResponse.setRefreshToken(refreshToken.getToken());

            return authResponse;

        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new TvdgException.InvalidCredentialsException("Invalid username/password:");
            } else if (e instanceof DisabledException) {
                throw new TvdgException.AccountDisabledException("Account disabled");
            } else {
                throw e;
            }
        }
    }


//    @Override
//    @Transactional
//    public LoginResponseDto login(String login, String password, HttpServletRequest request) {
//        try {
//            // Authenticate the user
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(login, password)
//            );
//
//            // Retrieve the user by login
//            User user = getUserByLogin(login);
//
//
//            // Ensure roles are initialized
//            user.getRoles().size();
//
//            // Logging to trace the roles
//            logger.debug("User roles: " + user.getRoles());
//
////            // Generate and send OTP for all users
////            String otp = CodeGeneratorUtils.generateOTP();
////            user.setOtp(otp);
////            userRepository.save(user);
////            sendOTP(user, otp);
////
////            // Respond to the client to verify OTP
////            return new LoginResponseDto("OTP verification required. Please check your phone for the OTP.");
//
//            // Check if the user is a rider and if OTP verification is required
//            if (user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("RIDER"))) {
//                logger.debug("User has RIDER role");
//
//                // Generate and send OTP
//                String otp = CodeGeneratorUtils.generateOTP();
//                user.setOtp(otp);
//                userRepository.save(user);
//                sendOTP(user, otp);
//
//                // Respond to the client to verify OTP
//                return new LoginResponseDto("OTP verification required. Please check your phone for the OTP.");
//            }
//
//            logger.debug("User does not have RIDER role");
//
//            // Generate JWT token
//            String token = tokenProvider.generateToken(authentication);
//            if (isEmptyToken(token)) {
//                throw new TvdgException.UnAuthorizeException("Invalid username/password:");
//            }
//
//            // Create a refresh token
//            UserRefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
//
//            // Update last login timestamp
//            user.setLastLogin(System.currentTimeMillis() / 1000L); // Unix timestamp
//            userRepository.save(user);
//
//
//                        // Extract additional session details
//            String deviceId = request.getHeader("Device-Id");
//            String deviceType = getDeviceType(request.getHeader("User-Agent"));
//            String ipAddress = request.getRemoteAddr();
//            String location = getLocationFromIp(ipAddress);
//            String os = getOperatingSystem(request.getHeader("User-Agent"));
//            String userAgent = request.getHeader("User-Agent");
//
//            // Store session information
//            ApiSession apiSession = new ApiSession();
//            apiSession.setUserId(user.getId());
//            apiSession.setToken(token);
//            apiSession.setDeviceId(deviceId);
//            apiSession.setDeviceType(DeviceType.valueOf(deviceType));
//            apiSession.setIpAddress(ipAddress);
//            apiSession.setLocation(location);
//            apiSession.setOs(os);
//            apiSession.setUserAgent(userAgent);
//            apiSession.setCreatedAt(new Date());
//            apiSession.setExpiresAt(new Date(System.currentTimeMillis() + 3600000)); // 1 hour session duration
//            apiSession.setActive(true);
//            apiSessionRepository.save(apiSession);
//            // Check if the user is a customer
//            Optional<CustomerUser> customerUserOpt = customerUserRepository.findCustomerUserById(user.getId());
//            Optional<Wallet> walletOpt = walletRepository.findByUserId(user.getId());
//
//            // Create the response object
//            LoginResponseDto authResponse = createLoginResponse(
//                    user,
//                    customerUserOpt.orElse(null),
//                    walletOpt.orElse(null),
//                    token
//            );
//            authResponse.setAuthorities(AuthUtils.buildAuthorities(authentication));
//            authResponse.setRefreshToken(refreshToken.getToken());
//
//            return authResponse;
//
//        } catch (Exception e) {
//            if (e instanceof BadCredentialsException) {
//                throw new TvdgException.InvalidCredentialsException("Invalid username/password:");
//            } else if (e instanceof DisabledException) {
//                throw new TvdgException.AccountDisabledException("Account disabled");
//            } else {
//                throw e;
//            }
//        }
//    }


    private String extractDeviceId(HttpServletRequest request) {
        return request.getHeader("Device-Id"); // Or wherever the device ID is stored
    }






//    @Transactional
//    public LoginResponseDto login(String login, String password, HttpServletRequest request) {
//        try {
//            // Authenticate the user
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(login, password)
//            );
//
//            // Retrieve user details
//            User user = getUserByLogin(login);
//
//            // Ensure roles are initialized
//            user.getRoles().size();
//
//            // Logging to trace the roles
//            logger.debug("User roles: " + user.getRoles());
//
//            // Check if the user is a rider and if OTP verification is required
//            if (user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("RIDER"))) {
//                logger.debug("User has RIDER role");
//
//                // Generate and send OTP
//                String otp = CodeGeneratorUtils.generateOTP();
//                user.setOtp(otp);
//                userRepository.save(user);
//                sendOTP(user, otp);
//
//                // Respond to the client to verify OTP
//                return new LoginResponseDto("OTP verification required. Please check your phone for the OTP.");
//            }
//
//            logger.debug("User does not have RIDER role");
//
//            // Proceed with generating the token and returning the response if not a rider
//            String token = tokenProvider.generateToken(authentication);
//            if (isEmptyToken(token)) {
//                throw new TvdgException.UnAuthorizeException("Invalid username/password:");
//            }
//
//            // Create refresh token
//            UserRefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
//
//            // Update last login timestamp
//            user.setLastLogin(System.currentTimeMillis() / 1000L); // Unix timestamp
//            userRepository.save(user);
//
//            // Extract additional session details
//            String deviceId = request.getHeader("Device-Id");
//            String deviceType = getDeviceType(request.getHeader("User-Agent"));
//            String ipAddress = request.getRemoteAddr();
//            String location = getLocationFromIp(ipAddress);
//            String os = getOperatingSystem(request.getHeader("User-Agent"));
//            String userAgent = request.getHeader("User-Agent");
//
//            // Store session information
//            ApiSession apiSession = new ApiSession();
//            apiSession.setUserId(user.getId());
//            apiSession.setToken(token);
//            apiSession.setDeviceId(deviceId);
//            apiSession.setDeviceType(DeviceType.valueOf(deviceType));
//            apiSession.setIpAddress(ipAddress);
//            apiSession.setLocation(location);
//            apiSession.setOs(os);
//            apiSession.setUserAgent(userAgent);
//            apiSession.setCreatedAt(new Date());
//            apiSession.setExpiresAt(new Date(System.currentTimeMillis() + 3600000)); // 1 hour session duration
//            apiSession.setActive(true);
//            apiSessionRepository.save(apiSession);
//
//            // Check if the user is a customer
//            Optional<CustomerUser> customerUserOpt = customerUserRepository.findCustomerUserById(user.getId());
//            Optional<Wallet> walletOpt = walletRepository.findByUserId(user.getId());
//
//            // Create response
//            LoginResponseDto authResponse = createLoginResponse(user, customerUserOpt.orElse(null), walletOpt.orElse(null), token);
//            authResponse.setAuthorities(AuthUtils.buildAuthorities(authentication));
//            authResponse.setRefreshToken(refreshToken.getToken());
//            return authResponse;
//
//        } catch (Exception e) {
//            if (e instanceof BadCredentialsException) {
//                throw new TvdgException.InvalidCredentialsException("Invalid username/password:");
//            } else if (e instanceof DisabledException) {
//                throw new TvdgException.AccountDisabledException("Account disabled");
//            } else {
//                throw e;
//            }
//        }
//    }

    private String getOperatingSystem(String userAgent) {
        if (userAgent != null) {
            if (userAgent.contains("Windows")) {
                return "Windows";
            } else if (userAgent.contains("Mac")) {
                return "Mac OS";
            } else if (userAgent.contains("Linux")) {
                return "Linux";
            } else if (userAgent.contains("Android")) {
                return "Android";
            } else if (userAgent.contains("iPhone")) {
                return "iOS";
            }
        }
        return "Unknown";
    }

    private String getDeviceType(String userAgent) {
        if (userAgent != null) {
            if (userAgent.contains("Mobile")) {
                return "mobile";
            } else if (userAgent.contains("Tablet")) {
                return "tablet";
            } else if (userAgent.contains("Desktop")) {
                return "desktop";
            }
        }
        return "other";
    }

    private String getLocationFromIp(String ipAddress) {
        // Placeholder logic for IP geolocation
        // Use an IP geolocation API or service to get location details
        // Example: return IpGeolocationService.getLocation(ipAddress);
        return "Location based on IP"; // Replace with actual implementation
    }


    private User getUserByLogin(String login) {
        if (login.contains("@")) {
            return userRepository.findByEmail(login)
                    .orElseThrow(() -> new InvalidCredentialsException("User with supplied credential does not exist"));
        } else {
            return userRepository.findByPhone(login)
                    .orElseThrow(() -> new InvalidCredentialsException("User with supplied credential does not exist"));
        }
    }

    @Transactional
    @Override
    public LoginResponseDto verifyOtp(String username, String otp, HttpServletRequest request) {
        // Detect if username is a phone number
//        boolean isPhoneNumber = username.matches("\\+?\\d+"); // Regex to check if username is a phone number
//        if (isPhoneNumber) {
//            // Append country code if not already present
//            String countryCode = "+234"; // Replace with the desired country code
//            if (!username.startsWith("+")) {
//                if (username.length() == 11) {
//                    username = countryCode + username.substring(1);
//                } else {
//                    username = countryCode + username;
//                }
//            }
//        }

//        User user = userRepository.findByTelephoneNumber(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));

        // Retrieve the user by login
        User user = getUserByLogin(username);


        if (otp.equals(user.getOtp())) {
            user.setOtp(null); // Clear OTP after verification
            userRepository.save(user);

            // Load user details to get authorities
            UserDetails userDetails = new SecuredUserInfo(user);

            // Proceed with generating the token and returning the response
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            String token = tokenProvider.generateToken(authentication);
            if (isEmptyToken(token)) {
                throw new TvdgException.UnAuthorizeException("Invalid username/password:");
            }

            UserRefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

            // Extract additional session details
            String deviceId = request.getHeader("Device-Id");
            String deviceType = getDeviceType(request.getHeader("User-Agent"));
            String ipAddress = request.getRemoteAddr();
            String location = getLocationFromIp(ipAddress);
            String os = getOperatingSystem(request.getHeader("User-Agent"));
            String userAgent = request.getHeader("User-Agent");

            // Store session information
            ApiSession apiSession = new ApiSession();
            apiSession.setUserId(user.getId());
            apiSession.setToken(token);
            apiSession.setDeviceId(deviceId);
            apiSession.setDeviceType(DeviceType.valueOf(deviceType));
            apiSession.setIpAddress(ipAddress);
            apiSession.setLocation(location);
            apiSession.setOs(os);
            apiSession.setUserAgent(userAgent);
            apiSession.setCreatedAt(new Date());
            apiSession.setExpiresAt(new Date(System.currentTimeMillis() + 3600000)); // 1 hour session duration
            apiSession.setActive(true);
            apiSessionRepository.save(apiSession);

//            // Create response
//            LoginResponseDto authResponse = createLoginResponse(user, token);
//            authResponse.setAuthorities(userDetails.getAuthorities().stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .collect(Collectors.toList()));
//            authResponse.setRefreshToken(refreshToken.getToken());
//            return authResponse;

                        // Check if the user is a customer
            Optional<CustomerUser> customerUserOpt = customerUserRepository.findCustomerUserById(user.getId());
            Optional<Wallet> walletOpt = walletRepository.findByUserId(user.getId());

            // Create the response object
            LoginResponseDto authResponse = createLoginResponse(
                    user,
                    customerUserOpt.orElse(null),
                    walletOpt.orElse(null),
                    token
            );
            authResponse.setAuthorities(AuthUtils.buildAuthorities(authentication));
            authResponse.setRefreshToken(refreshToken.getToken());

            return authResponse;
        }
        throw new RuntimeException("Invalid OTP.");
    }


    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordDto changePasswordDto) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(USER, email);
        }
        if (user.get().getPassword()==null||!this.checkIfValidOldPassword(changePasswordDto.getPassword(), user.get().getPassword())) {
            throw new InvalidRequestException("Invalid old password");
        }
        this.changePassword(user.get(), changePasswordDto.getNewPassword());

        //todo: do asynchrounously using queue
////        Cache cache=cacheManager.getCache("adminUserAuthInfo");
//        if(cache!=null) {
//            cache.evictIfPresent(email);
//        }
    }

    @Override
    @Transactional
    public void logout(String token, LogoutRequest logoutRequest) {
        Optional<UserRefreshToken> refreshToken = this.refreshTokenService.findByToken(logoutRequest.getRefreshToken());
        refreshToken.ifPresent(this.refreshTokenService::deleteToken);
        this.tokenBlacklistService.blacklistToken(token);
    }

    private void changePassword(User user, String newPassword) {
        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.userRepository.save(user);
    }

    private boolean checkIfValidOldPassword(final String oldPassword, final String userPassword) {
        return passwordEncoder.matches(oldPassword, userPassword);
    }

    private LoginResponseDto createLoginResponse(User user, CustomerUser customerUser, Wallet wallet, String token) {
        return AuthUtils.createLoginResponse(user, customerUser, wallet,  token);
    }
    private LoginResponseDto createLoginResponse(User user, String token) {
        return AuthUtils.createLoginResponse(user, token);
    }

    private boolean isEmptyToken(String token) {
        return AuthUtils.isEmptyToken(token);
    }

    // Method to send OTP to the user's phone
    public void sendOTP(User user, String otp) {
        try {
            this.emailTemplateUtils.sendCreateOtpEmail(user, otp);
        } catch (Exception e) {
            logger.error("Cannot send create admin user email", e);
        }
        // Implementation to send OTP (e.g., via SMS API)
    }

}
