package com.shopme.admin.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.webjars.NotFoundException;

import com.shopme.common.entity.Country;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTest {

	@Autowired CountryRepository repository;
	
	@Test
	public void testCreateCountry() {
		List<Country> countries =Arrays.asList(
				new Country("Indoneisa","ID"),
				new Country("United States","US"),
				new Country("Japan","JP"),
				new Country("Germany","DE"),
				new Country("Singapore","SG"));
		repository.saveAll(countries);
	}
	
	@Test
	public void testFindAllCountry() {
		List<Country>countries	=repository.findAllByOrderByNameAsc();
		countries.forEach(System.out::println);
	}
	@Test
	public void testUpdateCountry() {
		Integer countryId = 1;
		Country country =repository.findById(countryId).orElseThrow(()->
		 new NotFoundException("not found id"));
		
		country.setName("Indonesia");
		Country savedCountry = repository.save(country);
		assertThat(savedCountry.getName()).isEqualTo("Indonesia");
	}
	
	@Test
	public void getCountryById() {
		Integer countryId =1;
		Country country = repository.findById(countryId).orElseThrow(()->
		new NotFoundException("country is not exited"));
		assertThat(country).isNotNull();
		System.out.println(country);
		
	}
	
	@Test 
	public void deleteCountryById() {
		Integer countryId= 2;
		
		Country country = repository.findById(countryId).orElseThrow(() -> new NotFoundException("country is not existed"));
	    repository.deleteById(countryId);
	}
	
}
