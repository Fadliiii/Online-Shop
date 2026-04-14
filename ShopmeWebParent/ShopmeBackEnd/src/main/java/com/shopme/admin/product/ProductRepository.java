package com.shopme.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	public Long countById(Integer id);
	
	public Product findByName(String name);


	@Modifying
	@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id =?1")
	public void updateEnabledStatus(Integer id,boolean enabled);
	
	
	//LIKE %?1%"
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%"
			+ "OR p.shortDescription LIKE %?1%"
			+ "OR p.fullDescription LIKE %?1%"
			+ "OR p.alias LIKE %?1%"
			+ "OR p.brand.name LIKE %?1%"
			+ "OR p.category.name LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);
}
