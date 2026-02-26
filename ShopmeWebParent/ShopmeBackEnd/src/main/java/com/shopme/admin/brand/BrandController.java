package com.shopme.admin.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
@RequestMapping("/brands")
public class BrandController {
	@Autowired
	private BrandService service;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("")
	public String listBrand(Model model) {
		
		List<Brand> listtBrands = service.listBrand();
		model.addAttribute("listBrands", listtBrands);
		
		return "/brands/brands";
	}
	
	@GetMapping("/new")
	public String newBrand(Model model) {
		List<Category> listCategories =categoryService.listCategoriesUsedInForm();
	
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand",new Brand());
		model.addAttribute("pageTitle", "Create New Brand");
		
		return "brands/brand_form";
	}
}
