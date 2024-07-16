package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig<T> extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService( userService)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF protection for simplicity
            .authorizeRequests()
                .antMatchers("/auth/signup", "/auth/login").permitAll() // Allow everyone to access signup and login
                .anyRequest().authenticated() // All other requests need to be authenticated
            .and()
            .formLogin()
                .loginPage("/auth/login") // Specify the login page
                .defaultSuccessUrl("/home", true) // Redirect to home on success
                .permitAll() // Allow everyone to see the login page
            .and()
            .logout()
                .logoutSuccessUrl("/auth/login?logout") // Redirect to login page on logout
                .permitAll(); // Allow everyone to logout
    }
}
