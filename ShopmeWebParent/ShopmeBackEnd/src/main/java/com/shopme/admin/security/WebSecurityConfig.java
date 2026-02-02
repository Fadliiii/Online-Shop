package com.shopme.admin.security;

import java.text.Normalizer.Form;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopme.admin.ShopmeUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

	@Bean
	public UserDetailsService userDetailsService(){
		return new ShopmeUserDetailsService();
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.authenticationProvider(authenticationProvider());
		
		http.authorizeHttpRequests(
				auth -> auth
				.requestMatchers("/users/**").hasAuthority("Admin")
				.requestMatchers("/categories/**").hasAnyAuthority("Admin","Editor")				.anyRequest().authenticated()
				).formLogin(form -> form
						.loginPage("/login")
						.usernameParameter("email")
						.permitAll())
		
		.logout(logout -> logout.permitAll())
		.rememberMe(rem ->rem
				.key("AbcdefHijklMNOpQrstu_12321321312")
				.tokenValiditySeconds(7*24*60*60));
		return http.build();
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
