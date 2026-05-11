package com.shopme.admin.country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.webjars.NotFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTest {

	@Autowired MockMvc mockMvc;
	
	@Autowired ObjectMapper objectMapper;
	
	@Autowired CountryRepository countryRepository;
	
	@Test
	@WithMockUser(username = "fadlilatul.umamm@gmail.com",password = "umam2020",roles = "ADMIN")
	public void testListCountries() throws Exception{
		String url = "/countries/list";
		
	 MvcResult result =	mockMvc.perform(get(url)).andExpect(status().isOk())
		.andDo(print())
		.andReturn();
	 
	 String jsonResponse = result.getResponse().getContentAsString();
	// System.out.println(jsonResponse);
	 
	 Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);
//	 	for(Country country : countries) {
//	 		System.out.println(country);
//	 	}
	 	
	 	assertThat(countries).hasSizeGreaterThan(0);
	}
	
	
	@Test
	@WithMockUser(username = "fadlilatul.umamm@gmail.com",password = "umam2020",roles = "ADMIN")
	public void testCreateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		String countryName = "India";
		Country country = new Country(countryName);
		
	 MvcResult result = mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(country))
				.with(csrf()))
		.andDo(print()).andExpect(status().isOk())
		.andReturn();
	 
	 String response = result.getResponse().getContentAsString();
	 
	 Integer countryId = Integer.parseInt(response);
	 
	 Country savedCountry = countryRepository.findById(countryId).orElseThrow(() -> new NotFoundException("Country is not existed"));
	 assertThat(savedCountry.getName()).isEqualTo(countryName);
	 
	 //System.out.println("Country ID = "+response);
	}
	
	
	@Test
	@WithMockUser(username = "fadlilatul.umamm@gmail.com",password = "umam2020",roles = "ADMIN")
	public void testUpdateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save"; 
		Integer countryId = 6;
		String countryName = "Papua Nugini";
		String countryCode ="PN";
		Country country = new Country(countryId,countryName,countryCode);
		
	  mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(country))
				.with(csrf()))
		.andDo(print()).andExpect(status().isOk())
		.andExpect((ResultMatcher) content().string(String.valueOf(countryId)));
	 
		 
		 Country savedCountry = countryRepository.findById(countryId).orElseThrow(() -> new NotFoundException("Country is not existed"));
		 assertThat(savedCountry.getName()).isEqualTo(countryName);
		 
	 //System.out.println("Country ID = "+response);
	}
	
	@Test
	@WithMockUser(username = "fadlilatul.umamm@gmail.com",password = "umam2020",roles = "ADMIN")
	public void testDeleteCountry() throws JsonProcessingException, Exception {
		Integer countryId = 6;
		String url = "/countries/delete/"+countryId; 

		
	  mockMvc.perform(get(url)).andExpect(status().isOk());
	  Optional<Country> findById = countryRepository.findById(countryId);
	  assertThat(findById).isNotPresent();
	}
}
