package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

	@Autowired
	CategoryRepository categoryRepository;
	
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics");
		Category savedCategory = categoryRepository.save(category);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(7);
		Category memory= new Category("Iphone", parent);
		Category savedCategory = categoryRepository.save(memory);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	@Test
	public void testGetCategory() {
		Category category = categoryRepository.findById(2).get();
		System.out.println(category.getName());
		
		Set<Category> childern = category.getChildren();
		for(Category subCategory : childern) {
			System.out.println(subCategory.getName());
		}
		assertThat(childern.size()).isGreaterThan(0);
	}
	@Test
	public void testPrintHierarchicalCategory() {
		Iterable<Category>categories = categoryRepository.findAll();
		
		for(Category category : categories) {
				if( category.getParent() == null) {
					System.out.println(category.getName());
					
					Set<Category> childern = category.getChildren();
					
					for(Category subCategory : childern) {
						System.out.println("--"+subCategory.getName());	
				     	printChildern(subCategory, 1);
					}
				}
			
	}
}
	
		private void printChildern(Category parent,int subLevel) {	
			int newSubLevel = subLevel + 1;
			Set<Category> childern = parent.getChildren();
			
			for(Category subCategory : childern) {
				for (int i = 0; i < newSubLevel; i++) {
					System.out.print("--");
				}
				
				System.out.println(subCategory.getName());
			
			printChildern(subCategory, newSubLevel);
		}
	}

		@Test 
		public void testListRootCategories() {
			List<Category>rootCategories = categoryRepository.findRootCategories();
			
			rootCategories.forEach(cat -> System.out.println(cat.getName()));
		}
}
