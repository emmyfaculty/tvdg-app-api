package com.tvdgapp.securityconfig;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomBCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
        return auth.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "X-Requested-With"));
        config.setAllowedOrigins(List.of("http://127.0.0.1:5500"));
        config.setAllowedOrigins(List.of("http://localhost:5500"));
        config.setAllowedOrigins(List.of("https://192.168.64.3/"));
                // Allow any origin (optional, be cautious with credentials)
        config.addAllowedOriginPattern("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "X-Requested-With"));
//
//        // Add specific allowed origins
//        config.setAllowedOrigins(List.of(
//                "http://127.0.0.1:5500",
//                "http://localhost:5500",
////                "https://192.168.64.3:5500",
//                "https://192.168.64.3"
//        ));
//
//        // Allow any origin (optional, be cautious with credentials)
//        config.addAllowedOriginPattern("*");
//
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(HttpMethod.POST, "/api/v1/user/login", "/api/v1/customer/shipment", "/api/v1/nation_wide/shipments", "/api/v1/user/verify_otp").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/affiliate/signup","/api/v1/affiliate/login", "/api/v1/subscriptions/subscribe").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/admin/logout", "/api/v1/*", "/api/v1/shipments/calculate-shipping-options", "/api/v1/shipments/shipping_rate" ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/cities", "/api/v1/cities/*", "/api/v1/countries", "/api/v1/countries/*","/api/v1/states", "/api/v1/states/*", "/api/v1/states/**",
                                        "/api/v1/countries/**", "/api/v1/cities/**", "/api/v1/package-categories").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/customer/file_claim/{fileClaimsId}/receipt").permitAll()
                                .requestMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/swagger-ui/**", "/swagger-ui/index.html").permitAll()
                                .anyRequest().authenticated());
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
