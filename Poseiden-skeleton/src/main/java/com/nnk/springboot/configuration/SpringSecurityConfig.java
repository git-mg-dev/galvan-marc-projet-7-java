package com.nnk.springboot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                auth -> {
                    auth.requestMatchers("/login").permitAll();
                    auth.requestMatchers("/token").permitAll();
                    //auth.requestMatchers("/error/*").permitAll();
                    auth.requestMatchers("/home").hasRole("ADMIN");
                    auth.requestMatchers("/admin/*").hasRole("ADMIN");
                    auth.requestMatchers("/user/*").hasRole("ADMIN");
                    auth.requestMatchers("/bidList/*").hasRole("USER");
                    auth.requestMatchers("/curvePoint/*").hasRole("USER");
                    auth.requestMatchers("/rating/*").hasRole("USER");
                    auth.requestMatchers("/rule/*").hasRole("USER");
                    auth.requestMatchers("/trade/*").hasRole("USER");
                    auth.anyRequest().authenticated();
                })
                /*.formLogin(form -> form
                        .loginPage("/login")
                        //.loginProcessingUrl("/token")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                        .permitAll())*/
                /*.logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                )*/
                /*.exceptionHandling(
                        exception -> {
                            exception.accessDeniedHandler(accessDeniedHandler()).accessDeniedPage("/error/403");
                            exception.authenticationEntryPoint(entryPoint()).accessDeniedPage("/error/401");
                        }
                )*/
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    @Bean
    public AccessDeniedHandler accessDeniedHandler() { return new CustomAccessDeniedHandler(); }

    @Bean
    public AuthenticationEntryPoint entryPoint() { return new JwtAuthenticationEntryPoint(); }

     */
}
