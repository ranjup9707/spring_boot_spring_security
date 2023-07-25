package com.springboot.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboot.security.filter.JwtAuthFilter;
import com.springboot.security.service.UserInfoUserDetailService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //PreAuthorize in controller can only work with this annotation
public class SecurityConfiguration
{
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	/* Authentication - User Detail to be placed here */
	@Bean
	public UserDetailsService userDetailService() {
		return new UserInfoUserDetailService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	/* Restrication to api, etc */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf().disable()
		.authorizeHttpRequests().requestMatchers("/products/welcome", "/products/new", "/products/authenticate").permitAll() //Allowed to access by all
		.and().authorizeHttpRequests().requestMatchers("/products/**").authenticated().and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authenticationProvider(authenticationProvider())
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService());
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}	
}
