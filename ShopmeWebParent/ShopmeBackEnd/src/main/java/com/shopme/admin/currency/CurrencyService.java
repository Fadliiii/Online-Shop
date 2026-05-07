package com.shopme.admin.currency;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.shopme.common.entity.Currency;

@Service
public class CurrencyService {

	@Autowired private CurrencyRepository currencyRepository;
	
	public List<Currency> lisCurrencyByName(){
		return currencyRepository.findAllByOrderByNameAsc();
	}
	
	public Currency findById (Integer id) {
		return currencyRepository.findById(id).orElseThrow(()->{
			throw new NotFoundException("could not find id");
		});
	}
}
