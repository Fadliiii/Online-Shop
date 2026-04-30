package com.shopme.site.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;

@Service
public class ProductService {

	public static final int PRODUCTS_PER_PAGE= 10;
	
	@Autowired private ProductRepository productRepository;
	
	public Page<Product> listByCategory(int pageNum , Integer categoryId){
		String categoryIdMatch = "-"+categoryId+"-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		System.out.println("categoryId = " + categoryId);
		System.out.println("categoryIdMatch = " + categoryIdMatch);
		return productRepository.listByCategory(categoryId, categoryIdMatch, pageable);
	}
	
}
