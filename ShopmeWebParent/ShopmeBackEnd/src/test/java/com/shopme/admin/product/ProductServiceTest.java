package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.common.entity.Product;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

	@MockBean
	ProductRepository repository;
	
	@InjectMocks
	ProductService service;
	
	@Test
	public void checkUniqueInNewModeReturnDuplicate() {
		Integer id= null;
		String name="Legion 5i";
		Product product = new Product(name);
		Mockito.when(repository.findByName(name)).thenReturn(product);
		
		String result = service.checkUniqueProductName(id, "Legion 5i");
		
		assertThat(result).isEqualTo("DUPLICATE");
	}
	
	@Test
	public void checkUniqueInNewModeReturnOK() {
		Integer id= null;
		String name="Legion 5i";
		Product product = new Product(name);
		Mockito.when(repository.findByName(name)).thenReturn(null);
		
		String result = service.checkUniqueProductName(id, name);
		
		assertThat(result).isEqualTo("OK");
	}

	@Test
	public void checkUniqueInEditModeReturnDuplicate() {
		Integer id = 1;
		String name="legion 5i";
		
		Product product = new Product(name);
		product.setId(2);
		Mockito.when(repository.findByName(name)).thenReturn(product);
		
		String result = service.checkUniqueProductName(id, name);

		assertThat(result).isEqualTo("DUPLICATE");
		assertThat(id).isEqualTo(1);
	}
	
	@Test
	public void checkUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name="legion 5i";
		
		Product product = new Product(name);
		product.setId(1);
		Mockito.when(repository.findByName(name)).thenReturn(product);
		
		String result = service.checkUniqueProductName(id, "idepad gaming 3i");

		assertThat(result).isEqualTo("OK");
	}
}
