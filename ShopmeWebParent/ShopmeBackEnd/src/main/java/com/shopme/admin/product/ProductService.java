package com.shopme.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

		@Autowired
		ProductRepository repository;
		
		public List<Product> listAll(){
			return repository.findAll();
		}
		
		public Product saveProduct(Product product) {
			if(product.getId()==null) {
				product.setCreatedTime(new Date());
				
			}
			if(product.getAlias() == null || product.getAlias().isEmpty()) {
				String defaultAlias = product.getName().replace(" ","-");
		
				product.setAlias(defaultAlias);
			}else {
				product.setAlias(product.getAlias().replace(" ", "-"));
			}
		
			product.setUpdatedTime(new Date());
			
			return repository.save(product);
		}
		
		public String checkUniqueProductName(Integer id, String name) {
			boolean isCreatingNew=(id == null || id == 0);//kondisi awal null/tidak ada data, inputan null dan lenovo legion5i
			Product productByName = repository.findByName(name);// findByName = null karena data awal kosong
			if(isCreatingNew) {//true
				if(productByName != null) {// null!=null false
					return "DUPLICATE";
				}
			}else {
				if(productByName !=null && productByName.getId() != id ) {
					return "DUPLICATE";
				}
			}
			return "OK";
		}
		
		public void updateEnabledProduct( Integer id, boolean enabled) {
			repository.updateEnabledStatus(id, enabled);
		}
		
		public void deleteProduct(Integer id) throws ProductNotFoundException {
			Long countById = repository.countById(id);
			
			if(countById == null || countById == 0) {
				throw new ProductNotFoundException("product id = "+ id + " is not exist !");
			}
			
			repository.deleteById(id);
		}
		
		public Product get (Integer id) throws ProductNotFoundException {
			try {
			return repository.findById(id).get();
		
			}catch(NoSuchElementException ex){
				throw new ProductNotFoundException("Could not find any product with ID "+id);
			}

		}
}
