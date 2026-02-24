package com.shopme.admin.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;

@Service
public class BrandService {
	
	@Autowired
	private BrandRepository repository;
	
	public List<Brand> listBrand(){
		return repository.findAll();
	}
}
