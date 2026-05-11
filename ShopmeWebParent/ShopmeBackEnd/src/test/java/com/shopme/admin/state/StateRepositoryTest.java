package com.shopme.admin.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTest {

	@Autowired private StateRepository repository;
	@Autowired private TestEntityManager entityManager;
	@Test
	public void testCreateState() {
		Country country = entityManager.find(Country.class, 1);
//			List<State> states = Arrays.asList(
//					new State("DKI JAKARTA",country),
//					new State("BANDUNG",country),
//					new State("Padang",country),
//					new State("ACEH",country));
//			repository.saveAll(states);
			
			Iterable<State>iterable = repository.findByCountryOrderByNameAsc(country);
			iterable.forEach(System.out::println);
			assertThat(iterable).size().isEqualTo(4);
	}
}
