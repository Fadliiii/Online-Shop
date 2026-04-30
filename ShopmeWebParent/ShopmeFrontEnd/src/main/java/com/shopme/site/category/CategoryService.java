package com.shopme.site.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

@Service
public class CategoryService {
	@Autowired CategoryRepository categoryRepository;
	
	public List<Category> listNoChildernCategories(){
		List<Category> listNoChildernCategories = new ArrayList<>();
		
		List<Category>  listEnabledCategories = categoryRepository.findAllEnabled();
		listEnabledCategories.forEach(category ->{
			Set<Category> childern = category.getChildren();
			if(childern == null || childern.size() == 0) {
				listNoChildernCategories.add(category);
			}
		});
		return listNoChildernCategories;
		
	}
	
	public Category getCategory(String alias) throws CategoryNotFoundException {
	  Category category =	categoryRepository.findByAliasEnabled(alias);
	  if(category == null) {
		  throw new CategoryNotFoundException("Could not find any category by alias = "+alias);
	  }
	  return category;
	}
	
	public List<Category> getCategoryParents (Category child){
		List<Category> listParents = new ArrayList<>();
		
		Category parent = child.getParent();
		while(parent != null) {
			listParents.add(0, parent);
			parent = parent.getParent();
		}
		listParents.add(child);
		
		return listParents;
	}
}

