package com.shopme.site;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
		
		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
			
			httpSecurity.authorizeHttpRequests(
					auth -> auth
					.requestMatchers("/customer").authenticated()
					.requestMatchers("/","/login").permitAll()
					.anyRequest().authenticated()
					)
			.formLogin(form-> form
							.loginPage("/login")
							.usernameParameter("email")
							.permitAll())
			
			.logout(logout -> logout.permitAll())
			
			.rememberMe(rem -> rem 
					.key("dskalfhaslifhiasfoashoifkljh12323")
					.tokenValiditySeconds(14*24*60*60));
			return httpSecurity.build();
			
			
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web)-> web.ignoring()
				.requestMatchers(
						"/images/**",
						"/js/**",
						"/webjars/**",
						"/category-images/**",
						"/site-logo/**"
						);
	}
}
