package com.shopme.admin.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.shopme.admin.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMER_PER_PAGE = 10;

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {

		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMER_PER_PAGE, sort);

		if (keyword != null) {
			return customerRepository.findAll(keyword, pageable);
		}

		return customerRepository.findAll(pageable);
	}

	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepository.updateEnableStatus(id, enabled);
	}

	public Customer get(Integer id) {
		try {
			return customerRepository.getById(id);
		} catch (Exception e) {
			throw new NotFoundException("Could not find any customers with ID = " + id);
		}
	}

	public List<Country> listAllCountries(){
		return countryRepository.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(Integer id,String email) {
		Customer existCustomer =customerRepository.findByEmail(email);
		
		if(existCustomer != null && existCustomer.getId() != id) {
			// found another customer having the same email
			return false;
		}
		return true;
	}
	
	public void save(Customer customerInForm) {
		if(!customerInForm.getPassword().isEmpty()) {
			String encodePassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodePassword);
		}else {
			Customer customerInDb = customerRepository.findById(customerInForm.getId()).get();
			customerInForm.setPassword(customerInDb.getPassword());
		}
		customerRepository.save(customerInForm);
	}
	
	
	public void delete(Integer id) {
		Long count = customerRepository.countById(id);
		if(count == null || count == 0) {
			throw new NotFoundException("Could not find another customer with ID "+id);	
		}
		customerRepository.deleteById(id);
	}
}