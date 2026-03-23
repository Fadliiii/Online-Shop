package com.shopme.admin.product;

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
}
