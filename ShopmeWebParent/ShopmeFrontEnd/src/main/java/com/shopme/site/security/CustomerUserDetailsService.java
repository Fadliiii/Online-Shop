package com.shopme.site.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Customer;
import com.shopme.site.customer.CustomerRepository;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = customerRepository.findByEmail(email);

		if(customer != null) {
			return new CustomerUserDetails(customer);
		}
		 throw new UsernameNotFoundException("Could not find user with email: "+ email);
	}

}
