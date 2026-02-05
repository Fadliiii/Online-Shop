package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.Iterator;
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
		List<Category>findRootCategories = categoryRepository.findRootCategories();
		return listHierarchicalCategories(findRootCategories);
	}
	
	private List<Category>listHierarchicalCategories(List<Category>rootCategories){
		List<Category>herarchicalCategories = new ArrayList<>();
		
		for (Category rootCategory : rootCategories) {
			herarchicalCategories.add(Category.copyFull(rootCategory));
			
			Set<Category>childern = rootCategory.getChildren();
			
			for (Category subCategory : childern) {
				String name = "--"+subCategory.getName();
				
				herarchicalCategories.add(Category.copyFull(subCategory, name));
			
				listSubHierearchicalCategories(subCategory,1,herarchicalCategories);
			}
		}
		
		return herarchicalCategories;
	}

	
	private void listSubHierearchicalCategories(Category parent ,
			int subLevel,
			List<Category>herarchicalCategories) {
		
		Set<Category>childern = parent.getChildren();
			int newSubLevel = subLevel +1;
		for (Category subCategory : childern) {
			String name = "";
			for (int i = 0; i < newSubLevel;i++) {
				name += "--";	
			}
			name += subCategory.getName();
			
			herarchicalCategories.add(Category.copyFull(subCategory, name));

			listSubHierearchicalCategories(subCategory, newSubLevel,herarchicalCategories);
			
		}
	}
	
	public Category save (Category category) {
		return categoryRepository.save(category);
	}
	
	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = categoryRepository.findAll();

		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));
				
				Set<Category> childern = category.getChildren();

				for (Category subCategory : childern) {
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(),name));
					listSubCategoriesUsedInForm(categoriesUsedInForm,subCategory,1);
				}
			}

		}
		return categoriesUsedInForm;

	}
	
	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm,
			Category parent,
			int subLevel) {	
		int newSubLevel = subLevel + 1;
		Set<Category> childern = parent.getChildren();
		
		for(Category subCategory : childern) {
			String name = "--";
			for (int i = 0; i < newSubLevel; i++) {
				name += "";
			}
			name += subCategory.getName();
			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(),name));
		
		listSubCategoriesUsedInForm(categoriesUsedInForm,subCategory, newSubLevel);
	}
	
}
	

}
