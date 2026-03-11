package com.shopme.admin.brands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.Assert;
import org.webjars.NotFoundException;

import com.shopme.admin.brand.BrandRepository;
import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepostioryTest {
	
	@Autowired
	private BrandRepository repository;

	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateBrand() {
		Category categoryLaptop =entityManager.find(Category.class,2);
		Brand brand = new Brand();
		brand.setName("Acer");
		brand.setLogo("brand-logo.png");
		brand.addCategory(categoryLaptop);
		repository.save(brand);
		assertThat(brand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrandMultiCategories() {
		 Category categoryCellPhone = entityManager.find(Category.class,4);
		 Category categoryTablets = entityManager.find(Category.class,7);
		 Brand apple = new Brand("Apple", "brand-logo.png");
		 apple.addCategory(categoryCellPhone);
		 apple.addCategory(categoryTablets);
		 
		 repository.save(apple);
		assertThat(apple.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrandSamsung() {
		 Category categoryMemory = entityManager.find(Category.class,29);
		 Category categoryInternalHardDrive = entityManager.find(Category.class,24);
		 Brand samsung = new Brand("Samsung");
		 samsung.addCategory(categoryMemory);
		 samsung.addCategory(categoryInternalHardDrive);
		 
		 repository.save(samsung);
		assertThat(samsung.getCategories()).hasSize(2);
	}
	
	@Test
	public void findAllBrand() {
		List<Brand> listBrands = repository.findAll();
		listBrands.forEach(brand->System.out.print(brand));
	}
	
	@Test
	public void findByIdBrand() {
		Integer samsungId = 4;
		Brand brandGetById = repository.findById(samsungId)
				.orElseThrow(()->new RuntimeException("id is not found "+samsungId));
		System.out.println(brandGetById);
	}
	@Test
	public void updateBrandName(){
		Integer brandId = 3;
		Brand brand= repository.findById(brandId)
				.orElseThrow(()-> new RuntimeException("id is not found "+ brandId));
		brand.setName("Samsung Electronic");
	
		repository.save(brand);
		System.out.println(brand);
	}
	@Test
	public void deleteBrand() {
		Integer brandId = 2;
		Brand brand= repository.findById(brandId)
				.orElseThrow(()-> new RuntimeException("id is not found "+ brandId));
		
		repository.delete(brand);
		assertFalse(repository.findById(brandId).isPresent());
	}
	

}
