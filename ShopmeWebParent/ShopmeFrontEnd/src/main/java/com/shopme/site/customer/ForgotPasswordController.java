package com.shopme.site.customer;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.webjars.NotFoundException;

import com.shopme.common.entity.Customer;
import com.shopme.site.setting.EmailSettingBag;
import com.shopme.site.setting.SettingService;
import com.shopme.site.utill.Utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ForgotPasswordController {

	@Autowired private CustomerService  customerService;
	@Autowired private SettingService settingService;

	@GetMapping("/forgot_password")
	public String showRequestForm() {
		return "customer/forgot_password_form";
	}

	@PostMapping("/forgot_password")
	public String proccesRequestForm(HttpServletRequest request,Model model) throws UnsupportedEncodingException, MessagingException {
		String email = request.getParameter("email");
		
		try {
			String token = customerService.updateResetPasswordToken(email);
			String link =Utility.getSiteURL(request)+"/reset_password?token="+token;
			sendEmail(link, email);
			
			model.addAttribute("message", "We have sent a reset password link to your email."+
			"please check");
		}catch(NotFoundException e){
			model.addAttribute("error", e.getMessage());
		}catch(UnsupportedEncodingException | MessagingException e){
			model.addAttribute("error", "Could not send email");
		}
	
		return "customer/forgot_password_form";
	}
	
	
	private void sendEmail(String link,String email) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettingBag();
		JavaMailSenderImpl mailSender =	Utility.prepareMailSender(emailSettings);
	
		String toAddres = email;
		String subject ="Here's the link to reset your password";
		
		String content = "<p>Hello,</p>"
				+"<p>You have requested to reset your password.</p>"
				+"<p>Click the link below to change your password</p>"
				+"<p><a href=\"" + link + "\">Change my password</a></p>"
				+"<br>"
				+"<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p>";
		
		MimeMessage message =mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
	
		helper.setFrom(emailSettings.getFromAddress(),emailSettings.getSenderName());
		helper.setTo(toAddres);
		helper.setSubject(subject);
		
		helper.setText(content,true);
		
		mailSender.send(message);
	}
	
	@GetMapping("/reset_password")
	public String showResetForm(@Param("token")String token,Model model) {
		Customer customer = customerService.getByResetPasswordToken(token);
		if(customer != null) {
			model.addAttribute("token", token);
		}else {
			model.addAttribute("message", "invalid Token");
			model.addAttribute("pageTitle", "Invalid Token");
			return "message";
		}
		return "customer/reset_password_form";
	}
	
	@PostMapping("/reset_password")
	public String proccessResetForm(HttpServletRequest request,Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		try{
			customerService.updatePassword(token, password);
			model.addAttribute("message", "You have succesfully changed your password");
			model.addAttribute("title","Reset your password");
			model.addAttribute("pageTitle", "Reset your password");

			return "message";
		}catch(NotFoundException e){
			model.addAttribute("message", e.getMessage());
			model.addAttribute("pageTitle", "Invalid Token");
			return "message";
		}
		
	}
	
}
