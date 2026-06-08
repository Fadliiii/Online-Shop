package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.NotFoundException;

import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/brands")
public class BrandController {
	@Autowired
	private BrandService service;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("")
	public String listFirstPage() {
		return "redirect:/brands/page/1?sortField=name&sortDir=asc";
	}

	@GetMapping("/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listBrands",moduleURL = "/brands") PagingAndSortingHelper helper,
			@PathVariable(name ="pageNum")int pageNum
			) {
			service.listByPage(pageNum, helper);
		return "/brands/brands";
	}

	@GetMapping("/new")
	public String newBrand(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");

		return "brands/brand_form";
	}

	@PostMapping("/save")
	public String saveBrand(Brand brand, RedirectAttributes attributes,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

		try {
			service.save(brand, multipartFile);
			attributes.addFlashAttribute("message", "The brand has been saved sucessfully");
		} catch (Exception e) {
			attributes.addFlashAttribute("message", "Error while saving brand");
		}
		return "redirect:/brands";
	}

	@GetMapping("/edit/{id}")
	public String updateBrand(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {

		try {
			Brand brand = service.findById(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("brand", brand);
			model.addAttribute("pageTitle", "Edit Brand (ID : " + id + ")");

			return "brands/brand_form";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}

	}

	@GetMapping("/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "This Brand ID " + id + " has been deleted successfully");

		} catch (NotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/brands";
	}
}
