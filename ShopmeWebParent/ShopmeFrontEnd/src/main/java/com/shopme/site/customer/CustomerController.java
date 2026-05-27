package com.shopme.site.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.site.setting.EmailSettingBag;
import com.shopme.site.setting.SettingService;
import com.shopme.site.utill.Utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomerController {

	@Autowired CustomerService customerService;
	
	
	@Autowired SettingService  settingService;
	
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		List<Country>listAllCountries = customerService.listAllCountry();

		model.addAttribute("listCountries",listAllCountries);
		model.addAttribute("pageTitle", "Customer Registrasion");
		model.addAttribute("customer", new Customer());
		
		return "register/register_form";
	}
	
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer,
			Model model,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		customerService.registesrCustomer(customer);
		sendVerificationEmail(request,customer);
		model.addAttribute("pageTitle", "Registrasion Succeed!");
		
		return "/register/register_success";
	}

	private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException {

		EmailSettingBag emailSettings = settingService.getEmailSettingBag();
		JavaMailSenderImpl mailSender =	Utility.prepareMailSender(emailSettings);

		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubjecet();
		String content = emailSettings.getCustomerVerifyContent();
		
		MimeMessage message =mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
	
		helper.setFrom(emailSettings.getFromAddress(),emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", customer.getFullName());
		
		String verifyURL = Utility.getSiteURL(request)+"/verify?code="+customer.getVerificationCode();
	
		content = content.replace("[[URL]]", verifyURL);
		
		helper.setText(content,true);
		
		mailSender.send(message);

	}
	
	@GetMapping("/verify")
	public String verifyAccount (@Param("code")String code,Model model) {
	   boolean verified = customerService.verify(code);
	   
	   return "register/"+(verified? "verify_success" : "verify_fail"); 
	}
}
