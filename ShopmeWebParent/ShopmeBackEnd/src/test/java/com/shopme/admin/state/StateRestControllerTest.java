package com.shopme.admin.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.webjars.NotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.country.CountryRepository;
import com.shopme.common.dto.StateDTO;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTest {

	@Autowired MockMvc mockMvc;

	@Autowired ObjectMapper objectMapper;

	@Autowired CountryRepository countryRepository;
	
	@Autowired StateRepository repository;
	
	@Test
	@WithMockUser(username = "fadlilatul.umamm@gmail.com",password = "umam2020",roles = "ADMIN")
	public void testListState() throws Exception {
		String url = "/states/list";
		
		MvcResult result = (MvcResult) mockMvc.perform(get(url).param("countryId", "1"))
				.andExpect(status().isOk())
				.andDo(print()).andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
	//	System.out.println(jsonResponse);
		
		StateDTO[] stateDTO =objectMapper.readValue(jsonResponse, StateDTO[].class);
		for (StateDTO stateDTO2 : stateDTO) {
			System.out.println(stateDTO2);
		}
	}
	
	@Test
	@WithMockUser(username = "fadlilatul.umamm@gmail.com",password = "umam2020",roles = "ADMIN")
	public void testCreateState() throws Exception {
		String url = "/states/save";
		String stateName = "Bogor";
		Integer countryId =1;

		Country country = countryRepository.findById(countryId).orElseThrow(() -> new NotFoundException("country not existed"));
		
		State state = new State(stateName,country);
		
		MvcResult result =mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(state))
				.with(csrf()))
		.andDo(print()).andExpect(status().isOk())
		.andReturn();
	
	}

	
	@Test
	@WithMockUser(username = "fadlilatul.umamm@gmail.com",password = "umam2020",roles = "ADMIN")
	public void testUpdateState() throws Exception {
		String url = "/states/save";
		String stateName = "Yogyakarta";
		Integer stateId = 1;

		State state = repository.findById(stateId).get();
		state.setName("Yogyakarta");
		
		mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(state))
				.with(csrf()))
		.andDo(print()).andExpect(status().isOk()).andExpect(content().string(String.valueOf(stateId)));
		
		Optional<State> findById = repository.findById(stateId);
		
		assertThat(findById.isPresent());
		
		State updateState = findById.get();
		assertThat(updateState.getName()).isEqualTo(stateName);
		
	}
}
