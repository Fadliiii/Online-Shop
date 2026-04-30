package com.shopme.site.product;
import com.shopme.site.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
public class ProductService {

    private final WebSecurityConfig webSecurityConfig;

	public static final int PRODUCTS_PER_PAGE= 10;
	
	@Autowired private ProductRepository productRepository;

    ProductService(WebSecurityConfig webSecurityConfig) {
        this.webSecurityConfig = webSecurityConfig;
    }
	
	public Page<Product> listByCategory(int pageNum , Integer categoryId){
		String categoryIdMatch = "-"+categoryId+"-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		System.out.println("categoryId = " + categoryId);
		System.out.println("categoryIdMatch = " + categoryIdMatch);
		return productRepository.listByCategory(categoryId, categoryIdMatch, pageable);
	}
	
	public Product getProductByAlias(String alias) throws ProductNotFoundException {
		Product product = productRepository.findByAlias(alias);
		if(product == null) {
			throw new ProductNotFoundException("Could not find any product with alias ="+ alias);
		}
		return product;
	}
	
}
