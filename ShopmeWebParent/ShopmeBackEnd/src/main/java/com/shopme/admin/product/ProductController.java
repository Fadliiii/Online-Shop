package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService service;
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("")
	public String listFirstPage() {
		
		return "redirect:/products/page/1?sortField=name&sortDir=asc";
	}
	
	@GetMapping("/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listProducts",moduleURL = "/products") PagingAndSortingHelper helper,
			@PathVariable(name ="pageNum")int pageNum,
			Model model,
			@Param("categoryId")Integer categoryId
			) {
		
		service.listByPage(pageNum, helper,categoryId);
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
	
		if(categoryId != null) model.addAttribute("categoryId", categoryId);
		
		model.addAttribute("listCategories", listCategories);
		
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
		model.addAttribute("numberOfExistingExtraImages", 0);

		
		return "products/product_form";
	}
	
	@PostMapping("/save")
	public String saveProduct(Product product,RedirectAttributes attributes,
			@RequestParam(value = "fileImage",required = false)MultipartFile mainImageMultipart,
			@RequestParam(value ="extraImage",required = false)MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailsNames",required = false) String[] detailsNames,
			@RequestParam(name = "detailIDs",required = false) String[] detailIDs,
			@RequestParam(name = "detailsValues",required = false)String [] detailsValues,
			@RequestParam(name = "imageIDs",required = false)String [] imageIDs,
			@RequestParam(name = "imageNames",required = false)String [] imageNames,
			@AuthenticationPrincipal ShopmeUserDetails loogedUser)throws IOException {

		if(!loogedUser.hasRole("ADMIN") && !loogedUser.hasRole("EDITOR")) {
			if (loogedUser.hasRole("Salesperson")){
					service.saveProductPrice(product);
					attributes.addFlashAttribute("message","The product has been saved sucessfully.");
					return"redirect:/products";
			} 	
		}
			
	
		ProductSaveHelper.setMainImageName(mainImageMultipart,product);
		ProductSaveHelper.setExisttingExtraImageNames(imageIDs,imageNames,product);
		ProductSaveHelper.setNewExtraImagesNames(extraImageMultiparts,product);
		ProductSaveHelper.setProductDetails(detailIDs,detailsNames,detailsValues,product);
		
		Product savedProduct = service.saveProduct(product);
		
		ProductSaveHelper.saveUploadedImages(mainImageMultipart,extraImageMultiparts,savedProduct);
		
		ProductSaveHelper.deleteExtraImagesWeredRemoveOnForm(product);
		
		attributes.addFlashAttribute("message","The product has been saved sucessfully.");

		return"redirect:/products";
	}
	
	@GetMapping("/detail/{id}")
	public String getDetailProductById(@PathVariable("id") Integer id ,Model model,RedirectAttributes redirectAttributes) {
		try {
			Product product = service.get(id);

			model.addAttribute("pageTitle", "Product Details");
			model.addAttribute("product", product);
			return "products/product_modal_detail";
		}catch (ProductNotFoundException e){
			redirectAttributes.addFlashAttribute("message",e.getMessage());
		}
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
			
			String productExtraImagesDir= "../product-images/"+id+"/extras"; 
			FileUploadUtil.removeDir(productExtraImagesDir);
			
			String productImagesDir= "../product-images/"+id;
			FileUploadUtil.removeDir(productImagesDir);

			redirectAttributes.addFlashAttribute("message", "This product ID = " + id + " has been deleted successfully");

		} catch (NotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return"redirect:/products";
	}

	@GetMapping("/edit/{id}")
	public String editProduct(@PathVariable ("id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
		Product product = service.get(id);
		List<Brand>listBrands = brandService.listBrand();
		Integer numberOfExistingExtraImages =product.getImages().size();
		
		model.addAttribute("product",product);
		model.addAttribute("pageTitle","Edit Product (ID : "+id);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
	
		return "products/product_form";
		}catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	
	}
	
	
}
