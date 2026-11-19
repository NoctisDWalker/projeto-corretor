package com.nerdev.auxcorretor.config;

import com.nerdev.auxcorretor.security.TokenJwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final TokenJwtConverter tokenJwtConverter;

    // Cadeia da API. A cadeia @Order(1) (AuthorizationServerConfiguration)
    // cuida só dos endpoints OAuth2 e do /login.
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(
                                    "/auth/**",
                                    "/login",
                                    "/authorized",
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/swagger-ui.html", // Todo: Restringir por perfil do Spring (dev) quando for para produção
                                    "/").permitAll()
                            .requestMatchers("/clientes/**", "/imoveis/**", "/atendimentos/**").authenticated()
                            .anyRequest()
                            .denyAll();
                })
                .cors(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2RS -> {
                    oauth2RS.jwt(jwt -> jwt.jwtAuthenticationConverter(tokenJwtConverter));
                    oauth2RS.authenticationEntryPoint(authenticationEntryPoint);
                })
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
