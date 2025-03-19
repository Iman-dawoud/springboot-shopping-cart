package com.dailycodework.shoppingcart.security.config;



import com.dailycodework.shoppingcart.security.CustomAccessDeniedHandler;
import com.dailycodework.shoppingcart.security.jwt.AuthTokenFilter;
import com.dailycodework.shoppingcart.security.jwt.JwtAuthEntryPoint;
import com.dailycodework.shoppingcart.security.jwt.JwtUtils;
import com.dailycodework.shoppingcart.security.user.ShopUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class ShopConfig {
    private final ShopUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint authEntryPoint;
    private final JwtUtils jwtUtils;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    private final List<String> SECURED_URLS =
            List.of("/api/v1/carts/**", "/api/v1/cartItems/**");
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    //Configuration to give permission to the apis
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http.csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth ->auth.requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated()
//                .anyRequest().permitAll());
//
//        http.authenticationProvider(daoAuthenticationProvider());
//        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authEntryPoint) // Handles unauthorized requests
                        .accessDeniedHandler(customAccessDeniedHandler) // Custom Access Denied Handler
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/products/add",
                                "/api/v1/products/product/*/update",
                                "/api/v1/products/product/*/delete").hasRole("ADMIN") // Admin only
                        .requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated()
                        .anyRequest().permitAll()
                );

        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
