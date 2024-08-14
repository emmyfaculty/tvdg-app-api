//package com.tvdgapp.securityconfig;
//
//import com.tvdgapp.securityconfig.affiliate.AffiliateUserDetailsService;
//import com.tvdgapp.services.CustomerUserDetailsService;
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//@SuppressWarnings("NullAway.Init")
//public class MultipleEntryPointSecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://localhost:4001")); // Adjust port as needed
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
//        config.setAllowedHeaders(List.of("*")); // Adjust allowed headers as needed
//        config.setAllowCredentials(true); // Set to true if credentials are allowed
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }

    /*
     * For affiliate login
     */
//    @Configuration
//    @AllArgsConstructor
//    @Order(1)
//    public class AffiliateSecurityConfig {
//
//        private final AffiliateUserDetailsService userDetailsService;
//        private final JwtAuthenticationFilter jwtAuthenticationFilter;
//        private final PasswordEncoder passwordEncoder;
//        private final CorsConfigurationSource corsConfigurationSource;
////        @Bean
////        public PasswordEncoder passwordEncoder() {
////            return new  BCryptPasswordEncoder();
////        }
//
//        @Bean("affiliateUserAuthenticationManager")
//        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//            return configuration.getAuthenticationManager();
//        }
//
//        @Bean
//        public AuthenticationProvider authenticationProvider() {
//            DaoAuthenticationProvider authenticationProvider =new DaoAuthenticationProvider();
//            authenticationProvider.setUserDetailsService(userDetailsService);
//            authenticationProvider.setPasswordEncoder(passwordEncoder);
//            return authenticationProvider;
//        }
//
//
//
//        @Bean
//        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//            httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource));
//            httpSecurity.csrf(AbstractHttpConfigurer::disable)
//                    .authorizeHttpRequests(authorize ->
//                            authorize.requestMatchers(HttpMethod.POST, "/api/v1/affiliate/**", "/api/v1/affiliate/login").permitAll()
//                                    .requestMatchers(HttpMethod.POST, "/api/v1/affiliate/signup" ).permitAll()
////                                .requestMatchers(HttpMethod.GET, "/api/v1/user/*","/api/v1/ryder/*" ).permitAll()
//                                    .anyRequest().authenticated());
//            httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//            httpSecurity.authenticationProvider(authenticationProvider());
//            httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//            return httpSecurity.build();
//        }
//    }

//    @Configuration
//    @AllArgsConstructor
//    @Order(2)
//    public class CustomerSecurityConfig {
//
//        private final CustomerUserDetailsService userDetailsService;
//        private final JwtAuthenticationFilter jwtAuthenticationFilter;
//        private final PasswordEncoder passwordEncoder;
//        private final CorsConfigurationSource corsConfigurationSource;
//
//        @Bean("customerUserAuthenticationManager")
//        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//            return configuration.getAuthenticationManager();
//        }
//
//        @Bean
//        public AuthenticationProvider authenticationProvider() {
//            DaoAuthenticationProvider authenticationProvider =new DaoAuthenticationProvider();
//            authenticationProvider.setUserDetailsService(userDetailsService);
//            authenticationProvider.setPasswordEncoder(passwordEncoder);
//            return authenticationProvider;
//        }
//
////        @Bean
////        public CorsConfigurationSource corsConfigurationSource() {
////            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////            CorsConfiguration config = new CorsConfiguration();
////            config.setAllowedOrigins(List.of("http://localhost:4001")); // Adjust port as needed
////            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
////            config.setAllowedHeaders(List.of("*")); // Adjust allowed headers as needed
////            config.setAllowCredentials(true); // Set to true if credentials are allowed
////            source.registerCorsConfiguration("/**", config);
////            return source;
////        }
//
//        @Bean
//        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//            httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource));
//            httpSecurity.csrf(AbstractHttpConfigurer::disable)
//                    .authorizeHttpRequests(authorize ->
//                            authorize.requestMatchers(HttpMethod.POST, "/api/v1/customer/**", "/api/v1/customer/login").permitAll()
//                                    .requestMatchers(HttpMethod.POST, "/api/v1/customer/signup" ).permitAll()
////                                .requestMatchers(HttpMethod.GET, "/api/v1/user/*","/api/v1/ryder/*" ).permitAll()
//                                    .anyRequest().authenticated());
//            httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//            httpSecurity.authenticationProvider(authenticationProvider());
//            httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//            return httpSecurity.build();
//        }
//    }


//    @Configuration
//    @Order(4)
//    @AllArgsConstructor
//    public static class ApiDocWebSecurityConfigurerAdapter {
//        private final PasswordEncoder passwordEncoder;
//
//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.inMemoryAuthentication().withUser("user").password(passwordEncoder.encode("fellowship#")).roles();
//        }
//
//        private static final String[] AUTH_LIST = {
//                "/v2/api-docs",
//                "/configuration/ui",
//                "/swagger-resources",
//                "/configuration/security",
//                "/swagger-ui.html",
//                "/webjars/**"
//        };
//        @Bean
//        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//            httpSecurity
//                    .authorizeHttpRequests(authorize ->
//                            authorize.requestMatchers(HttpMethod.POST,  "/v2/api-docs",
//                                            "/configuration/ui",
//                                            "/swagger-resources",
//                                            "/configuration/security",
//                                            "/swagger-ui.html",
//                                            "/webjars/**").permitAll()
//                    .authorizeRequests().antMatchers(AUTH_LIST).authenticated()
//                    .and()
//                    .httpBasic().authenticationEntryPoint(swaggerAuthenticationEntryPoint())
//                    .and()
//                    .csrf().disable();
//        }
//
//        @Bean
//        public BasicAuthenticationEntryPoint swaggerAuthenticationEntryPoint() {
//            BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
//            entryPoint.setRealmName("Swagger Realm");
//            return entryPoint;
//        }
//    }

//}