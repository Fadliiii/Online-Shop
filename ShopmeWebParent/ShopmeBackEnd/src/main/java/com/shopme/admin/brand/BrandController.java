package com.shopme.admin.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shopme.common.entity.Brand;

@Controller
@RequestMapping("/brands")
public class BrandController {
	@Autowired
	private BrandService service;
	
	@GetMapping("")
	public String listBrand(Model model) {
		
		List<Brand> listtBrands = service.listBrand();
		model.addAttribute("listBrands", listtBrands);
		
		return "/brands/brands";
	}
}
