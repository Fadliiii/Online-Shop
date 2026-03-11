package com.shopme.admin.brand;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

public interface BrandRepository extends JpaRepository<Brand,Integer>{
    Long countById(Integer id);

	public Brand findByName(String name);

	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	public Page<Brand> findAll(String keyword,org.springframework.data.domain.Pageable pageable);
	
}
