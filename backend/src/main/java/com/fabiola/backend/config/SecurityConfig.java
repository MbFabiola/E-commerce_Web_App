package com.fabiola.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fabiola.backend.filter.JwtAuthenticationFilter;
import com.fabiola.backend.service.UserDetailsServiceImp;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final UserDetailsServiceImp userDetailsServiceImp;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final CustomLogoutHandler logoutHandler;

        public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp,
                              JwtAuthenticationFilter jwtAuthenticationFilter,
                              CustomLogoutHandler logoutHandler) {
                this.userDetailsServiceImp = userDetailsServiceImp;
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.logoutHandler = logoutHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(
                                req -> req.requestMatchers("/api/v1/auth/**", "/images/**")
                                        .permitAll()
                                        .requestMatchers("/api/v1/account/**")
                                        .hasAnyAuthority("ADMIN", "USER")
                                        .requestMatchers("/api/v1/admin/**")
                                        .hasAuthority("ADMIN")
                                        .requestMatchers("/api/v1/user/**").hasAuthority("USER")
                                        .anyRequest()
                                        .authenticated())
                        .userDetailsService(userDetailsServiceImp)
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                        .exceptionHandling(
                                e -> e.accessDeniedHandler(
                                                (request, response, accessDeniedException) -> response
                                                        .setStatus(403))
                                        .authenticationEntryPoint(new HttpStatusEntryPoint(
                                                HttpStatus.UNAUTHORIZED)))
                        .logout(l -> l
                                .logoutUrl("/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response,
                                                       authentication) -> SecurityContextHolder
                                        .clearContext()))
                        .build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));  // In production use specific origins
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                configuration.setMaxAge(3600L);
                // Note: setAllowCredentials(true) cannot be used with allowedOrigins("*")
                // If you need credentials support, specify exact origins instead

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
                return configuration.getAuthenticationManager();
        }
}