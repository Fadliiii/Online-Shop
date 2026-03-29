package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.webjars.NotFoundException;

import com.shopme.admin.FileUploadUtil;
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
	public String saveProduct(Product product,RedirectAttributes attributes,
			@RequestParam("fileImage")MultipartFile multipartFile) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
			product.setMainImage(fileName);
			
			Product savedProduct = service.saveProduct(product);
			String uploadDir= "../product-images/"+savedProduct.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		}else {
			service.saveProduct(product);
		}
		
		attributes.addFlashAttribute("message","The product has been saved sucessfully.");

		return"redirect:/products";
	}
	
	
	@GetMapping("/{id}/enabled/{enabled}")
	public String updateStatusEnabledProduct(@PathVariable Integer id,
			@PathVariable boolean enabled,
			RedirectAttributes redirectAttributes) {
		
		service.updateEnabledProduct(id, enabled);
		
		String status = enabled?"enabled" : "disabled";
		String message ="The Product ID "+id+" has been "+ status;
		redirectAttributes.addFlashAttribute("message", message);
		return"redirect:/products";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteProduct(@PathVariable("id") Integer id,
			RedirectAttributes redirectAttributes) throws ProductNotFoundException {
		try {
			service.deleteProduct(id);
			redirectAttributes.addFlashAttribute("message", "This product ID = " + id + " has been deleted successfully");

		} catch (NotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return"redirect:/products";
	}
}
