package com.nerdev.auxcorretor.config;

import com.nerdev.auxcorretor.security.CustomAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthorizationServerConfiguration {

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    @Order(1)
    public SecurityFilterChain authSecurityFilterChain (HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        http
                .securityMatcher(oAuth2AuthorizationServerConfigurer.getEndpointsMatcher())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .csrf(csrfConfigurer -> csrfConfigurer.ignoringRequestMatchers(
                        oAuth2AuthorizationServerConfigurer.getEndpointsMatcher()
                ))
                .with(oAuth2AuthorizationServerConfigurer, authorizationServer -> {
                    authorizationServer.oidc(Customizer.withDefaults());
                })
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                .cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH" ,"OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            Authentication principal = context.getPrincipal();
            String tokenType = context.getTokenType().getValue();

            if (principal instanceof CustomAuthentication custonAuth) {
                if (OAuth2TokenType.ACCESS_TOKEN.getValue().equals(tokenType)) {
                    Set<String> authorities = principal.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet());

                    context.getClaims().claim("roles", authorities);
                }
                if (OidcParameterNames.ID_TOKEN.equals(tokenType)) {
                    context.getClaims()
                            .claim("user_id", custonAuth.getUsuario().getId())
                            .claim("user_name", custonAuth.getUsuario().getNomeExibicao());
                }
            }
        };
    }

}
