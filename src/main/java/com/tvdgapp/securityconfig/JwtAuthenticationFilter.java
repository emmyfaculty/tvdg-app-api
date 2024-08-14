package com.tvdgapp.securityconfig;

import com.tvdgapp.models.session.ApiSession;
import com.tvdgapp.repositories.session.ApiSessionRepository;
import com.tvdgapp.services.auth.TokenBlacklistService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;
    private final ApiSessionRepository apiSessionRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   UserDetailsService userDetailsService,
                                   TokenBlacklistService tokenBlacklistService,
                                   ApiSessionRepository apiSessionRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.apiSessionRepository = apiSessionRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestUrl = request.getRequestURL().toString();
        boolean validToken = false;

        try {
            String token = jwtTokenProvider.extractTokenFromRequest(request);
            if (token != null && !token.isBlank() &&
                    !tokenBlacklistService.isBlacklisted(token) &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                Optional<ApiSession> apiSessionOptional = apiSessionRepository.findByToken(token);

                if (apiSessionOptional.isPresent() && apiSessionOptional.get().isActive()) {
                    String userName = jwtTokenProvider.extractUsername(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                    if (jwtTokenProvider.validateToken(token, userDetails.getUsername())) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        validToken = true;

                        // Update last used timestamp
                        ApiSession apiSession = apiSessionOptional.get();
                        apiSession.setLastUsedAt(new Date());
                        apiSessionRepository.save(apiSession);
                    } else {
                        SecurityContextHolder.clearContext();
                    }
                } else {
                    SecurityContextHolder.clearContext();
                }
            }
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            LOGGER.error("JWT token processing error: {}", e.getMessage());
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Unknown error has occurred");
            LOGGER.error("Unknown error", e);
            return;
        }

        if (requestUrl.contains("/api/v1/admin") && !validToken &&
                !requestUrl.contains("/api/v1/admin/refreshToken") &&
                !requestUrl.contains("/api/v1/admin/login")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            filterChain.doFilter(request, response);
        }
    }
}


//@Component
//@AllArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final UserDetailsService userDetailsService;
//    private final TokenBlacklistService tokenBlacklistService;
//    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
//
//            String requestUrl = request.getRequestURL().toString();
//            boolean validToken = false;
//            try {
//
//                String token = jwtTokenProvider.extractTokenFromRequest(request);
//                if (token != null && !token.isBlank()  && !tokenBlacklistService.isBlacklisted(castToNonNull(token))  && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    //extract the username from token
//                    String userName = jwtTokenProvider.extractUsername(token);
////                    String userType = jwtTokenProvider.extractUserTypeFromToken(token);//TODO: use url path to switch user type
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//                    if (jwtTokenProvider.validateToken(token, userDetails.getUsername())) {
//                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                                userDetails, null, userDetails.getAuthorities());
//                        usernamePasswordAuthenticationToken
//                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//                        validToken = true;
//                    } else {
//                        SecurityContextHolder.clearContext();
//                    }
//                }
//            } catch (JwtException e) {
//                SecurityContextHolder.clearContext();
//            } catch (Exception e) {
//                SecurityContextHolder.clearContext();
//                ApiResponseUtils.writeErrorResponse("Unknown error has occurred", response, HttpStatus.INTERNAL_SERVER_ERROR);
//                LOGGER.error("Unknown error", e);
//                return;
//            }
//
//            if (requestUrl.contains("/api/v1/admin") && !validToken && !requestUrl.contains("/api/v1/admin/refreshToken") && !requestUrl.contains("/api/v1/admin/login")) {
//                response.sendError(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//            } else {
//                filterChain.doFilter(request, response);
//            }
//        }

        /*
        * private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;
    private final ApiSessionRepository apiSessionRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestUrl = request.getRequestURL().toString();
        boolean validToken = false;
        try {
            String token = jwtTokenProvider.extractTokenFromRequest(request);
            if (token != null && !token.isBlank() && !tokenBlacklistService.isBlacklisted(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                Optional<ApiSession> apiSessionOptional = apiSessionRepository.findByToken(token);

                if (apiSessionOptional.isPresent() && apiSessionOptional.get().isActive()) {
                    String userName = jwtTokenProvider.extractUsername(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                    if (jwtTokenProvider.validateToken(token, userDetails.getUsername())) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        validToken = true;

                        // Update last used timestamp
                        ApiSession apiSession = apiSessionOptional.get();
                        apiSession.setLastUsedAt(new Date());
                        apiSessionRepository.save(apiSession);
                    } else {
                        SecurityContextHolder.clearContext();
                    }
                } else {
                    SecurityContextHolder.clearContext();
                }
            }
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            ApiResponseUtils.writeErrorResponse("Unknown error has occurred", response, HttpStatus.INTERNAL_SERVER_ERROR);
            LOGGER.error("Unknown error", e);
            return;
        }

        if (requestUrl.contains("/api/v1/admin") && !validToken && !requestUrl.contains("/api/v1/admin/refreshToken") && !requestUrl.contains("/api/v1/admin/login")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            filterChain.doFilter(request, response);
        }
    }

        *
        * */

//}
