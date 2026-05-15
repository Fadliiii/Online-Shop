package com.shopme.admin.state;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.dto.StateDTO;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Integer>{

	public List<State> findByCountryOrderByNameAsc(Country country);


}
