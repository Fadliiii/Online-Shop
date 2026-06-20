package com.shopme.site.customer;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.site.category.CategoryRepository;
import com.shopme.site.country.CountryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {

    private final CategoryRepository categoryRepository;

	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private CountryRepository countryRepository;
	@Autowired private CustomerRepository customerRepository;

    CustomerService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
	
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
		customer.setAuthenticationType(AuthenticationType.DATABASE);
		
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
	
	public void addNewCustomerUponAuthLogin(String name,String email,String countryCode,AuthenticationType authenticationType) {
		Customer customer = new Customer();
		customer.setEmail(email);
		
		setName(name,customer);
		
		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(authenticationType);
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
			
			String lastName = name.replaceFirst(firstName+" ","");
			customer.setLastName(lastName);
		}
	}
	
	public Customer getCustomerByEmail(String email) {
		return customerRepository.findByEmail(email);
	}
	
	public void update(Customer customerInForm) {

		Customer customerInDb = customerRepository.findById(customerInForm.getId()).get();

		if(customerInDb.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
		if(!customerInForm.getPassword().isEmpty()) {
			String encodePassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodePassword);
		}else {
			customerInForm.setPassword(customerInDb.getPassword());
		}
	}else {
		customerInForm.setPassword(customerInDb.getPassword());
	}
		
		customerInForm.setEnabled(customerInDb.isEnabled());
		customerInForm.setCreatedTime(customerInDb.getCreatedTime());
		customerInForm.setVerificationCode(customerInDb.getVerificationCode());
		customerInForm.setAuthenticationType(customerInDb.getAuthenticationType());
		customerInForm.setResetPasswordToken(customerInDb.getResetPasswordToken());
		customerRepository.save(customerInForm);
	}
	
	/**
	 * Generate and store a passwoord reset token for the customer
	 * associated with the given email address.
	 * 
	 * @param email the customer's registered email address
	 * @throws NotFoundException if no customer is found with the give email
	 */
	public String updateResetPasswordToken(String email) {
		Customer customer = customerRepository.findByEmail(email);
		if(customer != null) {
			String token = UUID.randomUUID().toString().replace("-","").substring(0,30);
			customer.setResetPasswordToken(token);
			customerRepository.save(customer);
			
			return token;
		}else {
			throw new NotFoundException("Could not find any customer with the email"+email);
		}
	}
	
	/**
	 * Get customer by reset token password
	 * @param resetPasswordToken customer
	 * @return Customer
	 * @throws NotFoundException  if no customer is found with the given token or token doesn't match with customer
	 */
	public Customer getByResetPasswordToken(String token) {
		Customer customer = customerRepository.findByResetPasswordToken(token);
		return customer;
	}
	
	/**
	 * Check token and store a new password
	 * @param token 
	 * @param newPassword
	 * @throws NotFoundException if no customer match with the token 
	 */
	public void updatePassword(String token, String newPassword) {
		Customer customer = customerRepository.findByResetPasswordToken(token);
		if(customer == null) {
			throw new NotFoundException("No customer a found: invalid token");
		}
		customer.setPassword(newPassword);
		customer.setResetPasswordToken(null);
		encodePassword(customer);
		customerRepository.save(customer);
	}
}
