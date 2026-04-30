package com.shopme.site;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.authorizeHttpRequests(
				auth -> auth.anyRequest().permitAll());
		return httpSecurity.build();
		
		
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web)-> web.ignoring()
				.requestMatchers(
						"/images/**",
						"/js/**",
						"/webjars/**"
						);
	}
}
