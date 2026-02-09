package com.shopme.admin.category;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.WebSecurityConfig;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.common.entity.Category;

@Service
public class CategoryService {

    private final WebSecurityConfig webSecurityConfig;

	@Autowired
	CategoryRepository categoryRepository;

    CategoryService(WebSecurityConfig webSecurityConfig) {
        this.webSecurityConfig = webSecurityConfig;
    }

	public List<Category> listAll(String sortDir) {
		
		Sort sort = Sort.by("name");
		
		 if(sortDir.equals("asc")) {
			sort = sort.ascending();
		}else if(sortDir.equals("desc")){
			sort = sort.descending();
		}
		List<Category>findRootCategories = categoryRepository.findRootCategories(sort);
		return listHierarchicalCategories(findRootCategories,sortDir);
	}
	
	private List<Category>listHierarchicalCategories(List<Category>rootCategories,String sortDir){
		List<Category>herarchicalCategories = new ArrayList<>();
		
		for (Category rootCategory : rootCategories) {
			herarchicalCategories.add(Category.copyFull(rootCategory));
			
			Set<Category>childern = sortSubCategories(rootCategory.getChildren(),sortDir);
			
			for (Category subCategory : childern) {
				String name = "--"+subCategory.getName();
				
				herarchicalCategories.add(Category.copyFull(subCategory, name));
			
				listSubHierearchicalCategories(subCategory,1,herarchicalCategories,sortDir);
			}
		}
		
		return herarchicalCategories;
	}

	
	private void listSubHierearchicalCategories(Category parent ,
			int subLevel,
			List<Category>herarchicalCategories,
			String sortDir){
		
		Set<Category>childern = sortSubCategories( parent.getChildren(),sortDir);
			int newSubLevel = subLevel +1;
		for (Category subCategory : childern) {
			String name = "";
			for (int i = 0; i < newSubLevel;i++) {
				name += "--";	
			}
			name += subCategory.getName();
			
			herarchicalCategories.add(Category.copyFull(subCategory, name));

			listSubHierearchicalCategories(subCategory, newSubLevel,herarchicalCategories,sortDir);
			
		}
	}
	
	public Category save (Category category,MultipartFile multipartFile) throws IOException {
		
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			
			
			Category savedCategory = categoryRepository.save(category);
			
			String uploadDir = "../category-images/"+savedCategory.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
			return savedCategory;
		}
			if (category.getImage()==null || category.getImage().isEmpty()) {
				category.setImage(null);
			}
		
		return categoryRepository.save(category);
	}
	
	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = categoryRepository.findRootCategories(Sort.by("name").ascending());

		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));
				
				Set<Category> childern = sortSubCategories(category.getChildren());

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
		Set<Category> childern =sortSubCategories( parent.getChildren());
		
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
	public Category  findById(int id) throws UserNotFoundException {
		try {
			return categoryRepository.findById(id).get();
		} catch (Exception e) {
			throw new  UserNotFoundException("Could not find id category with ID " + id);
		}
	}
	
	public String checkUnique (Integer id, String name, String alias) {
		boolean isCreatingNew = (id == null || id == 0);
		
		Category categoryByName = categoryRepository.findByName(name);
		
		if(isCreatingNew) {
			if(categoryByName != null) {
				return "Duplicate Name";
			}else {
			Category categoryByAlias = categoryRepository.findByAlias(alias);
				if(categoryByAlias != null) {
					return "Duplicate Alias";
				}
			}	
		}
		else {
			if(categoryByName !=null && categoryByName.getId() != id) {
				return "Duplicate Name";
			}
		
			Category categoryByAlias = categoryRepository.findByAlias(alias);
			if (categoryByAlias != null && categoryByAlias.getId() != id) {
				return "Duplicate Alias";
			}
		}
		
		
		return"OK";
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category>childern){
		return sortSubCategories(childern,"asc");
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category>childern,String sortDir){
		SortedSet<Category> sortedChildern = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category cat1, Category cat2) {
				System.out.println("sortDir = " + sortDir);
				if(sortDir.equals("asc")) {
				return cat1.getName().compareTo(cat2.getName());
				}else {
					return cat2.getName().compareTo(cat1.getName());
				}
			}
		});
		
		sortedChildern.addAll(childern);
		return sortedChildern;
	}

}
