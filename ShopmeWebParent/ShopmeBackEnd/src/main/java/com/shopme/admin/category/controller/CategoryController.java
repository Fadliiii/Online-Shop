package com.shopme.admin.category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		model.addAttribute("pageTitle","Create New Category");
		model.addAttribute("category", new Category());
		return "category/category_form";
	}
	
}
