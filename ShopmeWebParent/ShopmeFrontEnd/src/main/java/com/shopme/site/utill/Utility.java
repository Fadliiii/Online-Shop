package com.shopme.site.utill;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

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
}
