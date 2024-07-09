//package com.metinbudak.ecommerce.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class MySecurityConfig {
//    private final DataSource dataSource;
//
//    public MySecurityConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .httpBasic().disable()
//                .cors().and()
//                .authorizeHttpRequests()
////                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
////                .requestMatchers(HttpMethod.GET, "/info").hasRole("USER")
////                .requestMatchers("/users/**").hasAnyRole("ADMIN", "USER")
////                .requestMatchers("/admins").hasAuthority("ROLE_ADMIN")
////                .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")
////                .requestMatchers("/authenticate").permitAll()
//                .anyRequest().permitAll()
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        return http.build();
//    }
//
////    @Bean
////    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
////        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
////        authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource)
////                .usersByUsernameQuery("SELECT username, password, enabled" +
////                        " FROM users" +
////                        " WHERE username=?")
////                .authoritiesByUsernameQuery("SELECT username, authority" +
////                        " FROM authorities " +
////                        " WHERE username=?");
////        return authenticationManagerBuilder.build();
////    }
//
//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//
//        authenticationManagerBuilder.inMemoryAuthentication().withUser("user").password("{noop}geheim").roles("USER")
//                .and().withUser("admin").password("{noop}geheim").roles("ADMIN");
//        return authenticationManagerBuilder.build();
//    }
//
//
//}