package com.shopme.admin.category.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Category;

@Controller
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@GetMapping("")
	public String listAll(Model model,
			@Param("sortDir")String sortDir) {
	
		if(sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		
		List<Category>listCategories = categoryService.listAll(sortDir);
	
		String reverseSortDir = sortDir.equals("asc")?"desc":"asc";
		model.addAttribute("listCategories",listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		return"category/category";
	}
	@GetMapping("/new")
	public String newCategory(Model model) {
		List<Category>listCategories= categoryService.listCategoriesUsedInForm();
		model.addAttribute("pageTitle","Create New Category");
		model.addAttribute("category", new Category());
		model.addAttribute("listCategories",listCategories);

		return "category/category_form";
	}
	
	@PostMapping("/save")
	public String saveCatgory(Category category,
			RedirectAttributes attributes,
			@RequestParam("fileImage")MultipartFile multipartFile) throws IOException {
		
		try {
			categoryService.save(category, multipartFile);
			attributes.addFlashAttribute("message",
					"The category has been saved sucessfully");
		} catch (Exception e) {
			attributes.addFlashAttribute("message","Error while saving category");
		}
		return"redirect:/category";
	}
	
	@GetMapping("/edit/{id}")
	public String updateCategory(@PathVariable int id,
			Model model,
			RedirectAttributes redirectAttributes) {
		
		try {
			Category category = categoryService.findById(id);
			List<Category>listCategories= categoryService.listCategoriesUsedInForm();
			model.addAttribute("listCategories",listCategories);
			model.addAttribute("category", category);
			
			return"category/category_form";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/category";
		}
		
	}
	
}
