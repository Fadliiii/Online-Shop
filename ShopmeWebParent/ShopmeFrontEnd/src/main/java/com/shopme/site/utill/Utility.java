package com.shopme.site.utill;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.shopme.site.security.oauth.CustomerOauth2User;
import com.shopme.site.setting.EmailSettingBag;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {
	
	
	public static String getSiteURL(HttpServletRequest request) {
		String siteUrl = request.getRequestURL().toString();
		return 	siteUrl.replace(request.getServletPath(), "");
	}
	
	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPort());
		mailSender.setUsername(settings.getUsername());
		mailSender.setPassword(settings.getPassword());
		
		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());
		mailProperties.put("mail.smtp.ssl.trust", "*");
		mailSender.setJavaMailProperties(mailProperties);
		
		return mailSender;
	}
	
	public static String getEmailAuthenticatedCustomer (HttpServletRequest  request) {
		Object principal = request.getUserPrincipal();
		if(principal == null) return null;
		String customerEmail = null;

//		Object principal = request.getUserPrincipal();
//		String principalType = principal.getClass().getName();
//		// if login with password = usernamePasswordAuthenticationToken
//		// if login with google or facebook = OAuth2AuthenticationToken
//		// if login rememberme with password = remebermeAuthenticationToken
		
		if(principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			
			customerEmail = request.getUserPrincipal().getName();
		}else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
			CustomerOauth2User oauth2User = (CustomerOauth2User) oAuth2AuthenticationToken.getPrincipal();
			customerEmail = oauth2User.getEmail();
		}
		
		return customerEmail;
		
	}
}
