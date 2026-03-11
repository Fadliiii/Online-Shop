package com.shopme.admin.brands;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.admin.brand.BrandRepository;
import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTest {
	@MockBean
	private BrandRepository repository;
	
	@InjectMocks
	private BrandService service;
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicate() {
		Integer id = null;
		String name="Acer";
		
		Brand brand = new Brand(name);
		
		Mockito.when(repository.findByName(name)).thenReturn(brand);
		
		String result = service.checkUnique(id, name);
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnOk() {
		Integer id = 1;
		String name ="AMD";
		Brand brand = new Brand(name);
		Mockito.when(repository.findByName(name)).thenReturn(brand);
	
		String result = service.checkUnique(2, "Canon");
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicate() {
		Integer id=1;
		String name = "AMD";
		
		Brand brand = new Brand(name);
		
		Mockito.when(repository.findByName(name)).thenReturn(brand);
		
		String result = service.checkUnique(2, "AMD");
		
		assertThat(result).isEqualTo("Duplicate");
		
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOk() {
		Integer id=1;
		String name = "AMD";
		
		Brand brand = new Brand(name);
		
		Mockito.when(repository.findByName(name)).thenReturn(brand);
		
		String result = service.checkUnique(1, "sss");
		
		assertThat(result).isEqualTo("OK");
		
	}
}
