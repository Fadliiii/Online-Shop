package com.shopme.admin.state;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import com.shopme.admin.country.CountryRepository;
import com.shopme.common.dto.StateDTO;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@RestController
public class StateRestController {

	@Autowired StateRepository stateRepository;
	
	@Autowired CountryRepository countryRepository;
	
	@GetMapping("/states/list_by_country/{id}")
	public List<StateDTO> findAll(@PathVariable("id") Integer countryId){
		Country country = countryRepository.findById(countryId).orElseThrow(() -> new NotFoundException("Country does not existed"));
	    List<State> listStates =stateRepository.findByCountryOrderByNameAsc(country);
		return entityToDTO(listStates);
				
	}
	
	@PostMapping("/states/save")
	public String save(@RequestBody State state) {
		
		State savedState = stateRepository.save(state);

		return String.valueOf(savedState.getId());
	}
	
	@GetMapping("/states/delete/{id}")
	public void delete(@PathVariable("id") Integer id) {
		stateRepository.deleteById(id);
	}
	
	
	
	public List<StateDTO> entityToDTO(List<State> states){
		return states.stream()
				.map(state -> {
					StateDTO dto = new StateDTO();
					dto.setId(state.getId());
					dto.setName(state.getName());
					return dto;
				}).toList();
	}
}
