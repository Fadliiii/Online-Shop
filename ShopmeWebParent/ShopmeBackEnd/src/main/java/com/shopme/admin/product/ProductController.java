package com.shopme.admin.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shopme.common.entity.Product;

@Controller
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductService service;
	
	
	@GetMapping("")
	public String listAll(Model model) {
		List<Product> listProducts = service.listAll();
		
		model.addAttribute("listProducts", listProducts); 
		
		return "products/products";
	}
}
