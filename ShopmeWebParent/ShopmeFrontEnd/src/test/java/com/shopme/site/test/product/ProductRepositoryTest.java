package com.shopme.site.test.product;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

import com.shopme.common.entity.Product;
import com.shopme.site.product.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositoryTest {
	@Autowired
	private ProductRepository productRepository;
	
	@Test
	public void getPoductByCategory() {
		Integer id=10;
		
		Page<Product>products = productRepository.listByCategory(id, null, null);
		
		products.forEach(product -> {
			System.out.println(product.getName()+"("+product.isEnabled()+")");
		});
	}

}
