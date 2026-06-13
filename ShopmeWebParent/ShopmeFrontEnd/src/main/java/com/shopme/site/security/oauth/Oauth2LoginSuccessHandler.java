package com.shopme.site.security.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.site.customer.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Oauth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{

	@Autowired
	private CustomerService customerService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
	    CustomerOauth2User oauth2User =(CustomerOauth2User) authentication.getPrincipal();
	    String email = oauth2User.getEmail();
	    String name = oauth2User.getName();
	    String countryCode = request.getLocale().getCountry();
	    
	    Customer customer = customerService.getCustomerByEmail(email);
	    
	    if(customer == null) {
	    	customerService.addNewCustomerUponAuthLogin(name,email,countryCode);
	    }else {
	    	customerService.updateAuthentication(customer, AuthenticationType.GOOGLE);
	    }
	    super.onAuthenticationSuccess(request, response, authentication);
	}

}
