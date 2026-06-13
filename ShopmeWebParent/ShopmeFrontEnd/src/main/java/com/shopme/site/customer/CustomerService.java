package com.shopme.site.customer;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.site.country.CountryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {

	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private CountryRepository countryRepository;
	@Autowired private CustomerRepository customerRepository;
	
	public List<Country> listAllCountry(){
	 return	countryRepository.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepository.findByEmail(email);
		return customer == null;
	}
	
	public void registesrCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		
		String randomCode =  UUID.randomUUID().toString().replace("-", "");
		customer.setVerificationCode(randomCode);
		
		customerRepository.save(customer);
	}

	private void encodePassword(Customer customer) {
	   String encodedPassword =	passwordEncoder.encode(customer.getPassword());
	   customer.setPassword(encodedPassword);
	}
	
	public boolean verify(String verificationCode) {
	    Customer customer =	customerRepository.findByVerificationCode(verificationCode);
		
	    if(customer == null || customer.isEnabled()) {
	    	return false;
	    }else {
			customerRepository.enable(customer.getId());
			return true;
		}
	}
	
	public void updateAuthentication(Customer customer , AuthenticationType type) {
		if(!customer.getAuthenticationType().equals(type)) {
			customerRepository.updateAuthenticationType(customer.getId(), type);
		}
		
	}
	
	public void addNewCustomerUponAuthLogin(String name,String email,String countryCode) {
		Customer customer = new Customer();
		customer.setEmail(email);
		
		setName(name,customer);
		
		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.GOOGLE);
		customer.setPassword("");
		customer.setAddressLine1("");
		customer.setCity("");
		customer.setState("");
		customer.setPhoneNumber("");
		customer.setPostalCode("");
		customer.setCountry(countryRepository.findByCode(countryCode));

		customerRepository.save(customer);
	}
	
	private void setName(String name,Customer customer) {
		String[] nameArray = name.split(" ");
		if(nameArray.length <2) {
			customer.setFirstName(name);
			customer.setLastName("");
		}else {
			String firstName = nameArray[0];
			customer.setFirstName(firstName);
			
			String lastName = name.replaceFirst(firstName,"");
			customer.setLastName(lastName);
		}
	}
	
	public Customer getCustomerByEmail(String email) {
		return customerRepository.findByEmail(email);
	}
}
