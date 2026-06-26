package com.shopme.site.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.site.security.CustomerUserDetails;
import com.shopme.site.security.oauth.CustomerOauth2User;
import com.shopme.site.security.oauth.CustomerOauth2UserService;
import com.shopme.site.setting.EmailSettingBag;
import com.shopme.site.setting.SettingService;
import com.shopme.site.utill.Utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;


@Controller
public class CustomerController {

    private final CustomerOauth2UserService customerOauth2UserService;

	@Autowired CustomerService customerService;
	
	
	@Autowired SettingService  settingService;


    CustomerController(CustomerOauth2UserService customerOauth2UserService) {
        this.customerOauth2UserService = customerOauth2UserService;
    }
	
	
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
	
	@GetMapping("/account_details")
	public String viewAccountDetails(Model model,HttpServletRequest request) {
		
		String emailCustomer =	Utility.getEmailAuthenticatedCustomer(request);
		Customer customer = customerService.getCustomerByEmail(emailCustomer);
	    List<Country> listCountries =customerService.listAllCountry();
			model.addAttribute("customer", customer);
			model.addAttribute("listCountries", listCountries);
			return "customer/account_form";
	}
	
	
	
	@PostMapping("/update_accounts_details")
	public String updateAccountDetails (Model model,Customer customer,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		customerService.update(customer);
		
		redirectAttributes.addFlashAttribute("message", "Your account details have been upadated.");
		
		updateNameForAuthenticatedCustomer(customer, request);
		
		return "redirect:/account_details";
		
	}
	private void updateNameForAuthenticatedCustomer(Customer customer,HttpServletRequest request) {
		
		Object principal  = request.getUserPrincipal(); 
		
		if(principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			CustomerUserDetails userDetails = getCustomerUserDetails(principal);
			Customer authenticatedCustomer = userDetails.getCustomer();
			authenticatedCustomer.setFirstName(customer.getFirstName());
			authenticatedCustomer.setLastName(customer.getLastName());
			
		}else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
			CustomerOauth2User oauth2User = (CustomerOauth2User) oAuth2AuthenticationToken.getPrincipal();
			String fullName = customer.getFirstName() + " " + customer.getLastName();
			oauth2User.setFullName(fullName);
		}
		
	}
	
	private CustomerUserDetails getCustomerUserDetails(Object principal) {
		CustomerUserDetails userDetails = null;
		
		if(principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}else if(principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}
		
		return userDetails;
	}
	
}
