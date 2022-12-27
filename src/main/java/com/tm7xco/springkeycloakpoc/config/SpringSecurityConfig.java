package com.tm7xco.springkeycloakpoc.config;

import com.tm7xco.springkeycloakpoc.security.KeycloakAssignedRolesConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.tm7xco.springkeycloakpoc.security.UserRole.*;

@Configuration
public class SpringSecurityConfig {

    @Value("${cors-policies}")
    private String[] corsPolicies;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/user/register").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/user/update-password").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/user").authenticated()
                .requestMatchers("/api/business/user/*").hasAnyRole(USER.name(), SUPER_USER.name(), ADMIN.name())
                .requestMatchers("/api/business/super-user/*").hasAnyRole(SUPER_USER.name(), ADMIN.name())
                .requestMatchers("/api/business/admin/*").hasRole(ADMIN.name())
                .anyRequest().denyAll().and()
                .oauth2ResourceServer()
                .jwt();

        return http.build();
    }

    @Bean
    protected JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakAssignedRolesConverter());
        return jwtConverter;
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList(corsPolicies));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-Requested-With"));

        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }

}
