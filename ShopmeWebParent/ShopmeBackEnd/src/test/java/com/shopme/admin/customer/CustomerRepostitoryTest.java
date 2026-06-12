package com.shopme.admin.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepostitoryTest {

	@Autowired private CustomerRepository repository;
	@Autowired private TestEntityManager entityManager;


	@Test
	public void testCreateCustomer1() {
		Integer countryId=234;//USA
		
		Country country = entityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		
		customer.setCountry(country);
		customer.setFirstName("David");
		customer.setLastName("Fountaine");
		customer.setPassword("password123");
		customer.setEmail("david.s.fountaine@gmail.com");
		customer.setPhoneNumber("312-462-75178");
		customer.setAddressLine1("1927 West Dive");
		customer.setCity("Sacramento");
		customer.setState("California");
		customer.setPostalCode("95867");
		customer.setCreatedTime(new Date());
		
		Customer savedCustomer = repository.save(customer);
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateCustomer2() {
		Integer countryId=106;//USA
		
		Country country = entityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		
		customer.setCountry(country);
		customer.setFirstName("Sanya");
		customer.setLastName("Lad");
		customer.setPassword("password456");
		customer.setEmail("sanya.lad2020@gmail.com");
		customer.setPhoneNumber("02224928052");
		customer.setAddressLine1("173 , A- , Shah & Nahar Indl.estate, Sunmail Road");
		customer.setCity("Mumbai");
		customer.setState("Maharasthra");
		customer.setPostalCode("400013");
		customer.setCreatedTime(new Date());
		
		Customer savedCustomer = repository.save(customer);
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListCustomer() {
		Iterable<Customer> customers = repository.findAll();
		
		customers.forEach(System.out::println);
		
		assertThat(customers).hasSizeGreaterThan(1);
	}
	
	@Test
	public void testUpdateCustomer() {
		Integer customerId = 1;
		
		String lastName ="Stanfield";
		
		Customer customer = repository.findById(customerId).get();
		
		customer.setLastName(lastName);
		customer.setEnabled(true);
		
		Customer updatedCustomer = repository.save(customer);
		
		assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
	}
	
	@Test
	public void testGetCustomer() {
		Integer customerId =2 ;
		
		Optional<Customer> customer = repository.findById(customerId);
		
		assertThat(customer).isPresent();
		
		Customer customer2 = customer.get();
		System.out.println(customer2);
		
	}
	
	@Test
	public void testDeleteCustomer() {
		Integer customerId = 2;
		
		repository.deleteById(customerId);
		Optional<Customer>optionalCustomer = repository.findById(customerId);
		
		assertThat(optionalCustomer).isNotPresent();
		
	}
	
	@Test
	public void testFindByEmail() {
		String email ="david.s.fountaine@gmail.com";
		
		Customer customer = repository.findByEmail(email);
		assertThat(customer).isNotNull();
		
	}
	
//	@Test
//	public void testFindByCode() {
//		String code = "code_123";
//	    Customer customer =	repository.findByVerificationCode(code);
//	    assertThat(customer).isNotNull();
//	    System.out.println(customer);
//	}
//	@Test
//	public void tesetEnabaleCustomer() { 
//		Integer customerId =1;
//		
//		repository.enable(customerId);
//		
//		Customer customer = repository.findById(customerId).get();
//		assertThat(customer.isEnabled()).isTrue();
//		System.out.println(customer);
//	}
}
