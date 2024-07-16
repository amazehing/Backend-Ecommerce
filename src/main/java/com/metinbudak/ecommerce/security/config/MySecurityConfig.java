package com.metinbudak.ecommerce.security.config;

import com.metinbudak.ecommerce.security.filters.BasicAuthRequestFilter;
import com.metinbudak.ecommerce.security.filters.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class MySecurityConfig {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    private final JwtRequestFilter jwtRequestFilter;
    private final BasicAuthRequestFilter basicAuthRequestFilter;

    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                // H2-console enabled voor testen
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()

                // Alleen admins mogen alle users ophalen
                .requestMatchers(HttpMethod.GET, "/users").hasRole(ROLE_ADMIN)

                // Alleen admins mogen categorieen/producten/images aanmaken/updaten
                .requestMatchers(HttpMethod.POST, "/categories", "/categories/{id}/products").hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.PUT, "/categories/{id}", "/products/{id}").hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.POST, "/images").hasRole(ROLE_ADMIN)

                // Alleen geautoriseerde gebruikers kunnen data van zichzelf opvragen
                .requestMatchers(HttpMethod.GET, "/users/me", "/users/me/**").authenticated()

                // Alleen gebruikers mogen reviews plaatsen
                .requestMatchers(HttpMethod.POST, "/products/{id}/reviews").hasRole(ROLE_USER)

                // Alleen admins mogen data verwijderen
                .requestMatchers(HttpMethod.DELETE, "/**").hasRole(ROLE_ADMIN)

                // Voor de overige API's is authenticatie niet vereist
                .anyRequest().permitAll()

                // frameOptions anders is de h2-console niet zichtbaar
                .and().headers().frameOptions().sameOrigin()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http
                .addFilterBefore(basicAuthRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}