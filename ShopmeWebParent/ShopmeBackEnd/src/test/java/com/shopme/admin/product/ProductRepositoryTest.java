package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Date;
import java.util.Locale.Category;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.webjars.NotFoundException;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private EntityManager entityManager;
	
	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 37);
		com.shopme.common.entity.Category category = entityManager.find(com.shopme.common.entity.Category.class, 5);
		
		Product product = new Product();
		product.setName("Acer Aspire Desktop");
		product.setAlias("acer_aspire_dekstop");
		product.setShortDescription("Short description from Acer aspire");
		product.setFullDescription("Full description from Acer Aspire");
		
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(678);
		product.setCost(600);
		product.setEnabled(true);
		product.setInStock(true);
		
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		
		Product savedProduct = repository.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProduct = repository.findAll();

		iterableProduct.forEach(System.out::println);
	}
	
	@Test
	public void testGetProduct() {
		Integer id = 2;
		
		Product product = repository.findById(2).orElseThrow(()-> new NotFoundException("product doesnt exist"));
		System.out.println(product);
		
		assertThat(product).isNotNull();
	}
	
	@Test
	public void testUpdateProduct() {
		Integer id = 1;
		Product product = repository.findById(id).get();

		product.setPrice(499);
		
		repository.save(product);
		
		Product updatedProduct = entityManager.find(Product.class, id);
		
		assertThat(updatedProduct.getPrice()).isEqualTo(499);
	}
	
	@Test
	public void testDeleteProduct() {
		Integer id = 3;
		repository.deleteById(id);
		
		Optional<Product> result = repository.findById(id);
		assertThat(!result.isPresent());
	}
}