package com.shopme.admin.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

@Controller
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductService service;
	@Autowired
	private BrandService brandService;
	
	@GetMapping("")
	public String listAll(Model model) {
		List<Product> listProducts = service.listAll();
		
		model.addAttribute("listProducts", listProducts); 
		
		return "products/products";
	}
	
	@GetMapping("/new")
	public String newProduct(Model model) {
		
		List<Brand>listBrands = brandService.listBrand();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product", product);
		model.addAttribute("listBrands",listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		
		return "products/product_form";
	}
	
	@PostMapping("/save")
	public String saveProduct(Product product) {
		System.out.println("Product Name : "+product.getName());
		System.out.println("Brand id : "+product.getBrand().getId());
		System.out.println("Category id : "+product.getCategory().getId());

		return"redirect:/products";

	}
}
