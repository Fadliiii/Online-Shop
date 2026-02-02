package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	public List<Category> listAll() {
		return categoryRepository.findAll();
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = categoryRepository.findAll();

		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(new Category(category.getName()));
				
				Set<Category> childern = category.getChildren();

				for (Category subCategory : childern) {
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(new Category(name));
					listChildern(categoriesUsedInForm,subCategory,1);
				}
			}

		}
		return categoriesUsedInForm;

	}
	
	private void listChildern(List<Category> categoriesUsedInForm,Category parent,int subLevel) {	
		int newSubLevel = subLevel + 1;
		Set<Category> childern = parent.getChildren();
		
		for(Category subCategory : childern) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "";
			}
			name += subCategory.getName();
			categoriesUsedInForm.add(new Category(name));
		
		listChildern(categoriesUsedInForm,subCategory, newSubLevel);
	}
}

}
