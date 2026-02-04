package com.shopme.admin.category.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
	public String listAll(Model model) {
		List<Category>listCategories = categoryService.listAll();
		model.addAttribute("listCategories",listCategories);
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
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		category.setImage(fileName);
		
		
		Category savedCategory = categoryService.save(category);
		String uploadDir = "../category-images/"+savedCategory.getId();
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
		attributes.addFlashAttribute("message","The category has been saved successfully");
		
		
		return "redirect:/category";
	}
	
}
