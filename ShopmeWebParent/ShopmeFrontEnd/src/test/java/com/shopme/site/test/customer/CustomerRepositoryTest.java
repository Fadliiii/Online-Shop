package com.shopme.site.test.customer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.site.customer.CustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTest {

	@Autowired  private CustomerRepository repository;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testUpdateAuthenticationType() {
		Integer id = 1;
		repository.updateAuthenticationType(1, AuthenticationType.DATABASE);
		
	    Customer customer =	repository.findById(id).get();
	    assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);
	}
}
