package com.shopme.admin.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;

@Service
public class ProductService {

		@Autowired
		ProductRepository repository;
		
		public List<Product> listAll(){
			return repository.findAll();
		}
}
