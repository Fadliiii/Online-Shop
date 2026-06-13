package com.shopme.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopme.site.security.oauth.CustomerOauth2UserService;
import com.shopme.site.security.oauth.Oauth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired private CustomerOauth2UserService oauth2UserService;
	@Autowired private Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;
	
	
		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
			
			httpSecurity.authorizeHttpRequests(
					auth -> auth
					.requestMatchers("/customer").authenticated()
					.anyRequest().permitAll()
					)
			.formLogin(form-> form
							.loginPage("/login")
							.usernameParameter("email")
							.permitAll())
			.oauth2Login(oauth -> oauth
					.loginPage("/login")
					.userInfoEndpoint(userInfo ->{
						userInfo.userService(oauth2UserService);
					})
					.successHandler(oauth2LoginSuccessHandler))
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
						"/site-logo/**",
						"/fontawesome/**",
						"/webfonts/**"
						);
	}
}
