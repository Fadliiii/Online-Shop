package com.shopme.admin.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.NotFoundException;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@Controller
public class CustomerController {
	@Autowired private CustomerService customerService;
	
	@GetMapping("/customers")
	public String listFirstPage() {
		return "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
	}
	
	
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(
		@PagingAndSortingParam(listName = "listCustomers",moduleURL = "/customers") PagingAndSortingHelper helper,
		@PathVariable(name="pageNum")int pageNum) {
	
		customerService.listByPage(pageNum, helper);

	    return "customers/customers";
	}
	
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomersEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		customerService.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "enabled":"disabled";
		String message = "The customer ID "+id+" has been "+status;
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/customers";
	}
	
	
	@GetMapping("/customers/detail/{id}")
	public String viewCustomers(@PathVariable("id")Integer id,Model model,RedirectAttributes ra) {
		try {
		Customer customer = customerService.get(id);
		 model.addAttribute("customer", customer);
		 model.addAttribute("pageTitle", "Customer details");
		 return"customers/customer_detail_modal";
		}catch (Exception ex){
			ra.addFlashAttribute("message",ex.getMessage());
			return "redirect:/customers";
		}
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id")Integer id, Model model,RedirectAttributes ra) {
		try {
			Customer customer = customerService.get(id);
			List<Country>countries = customerService.listAllCountries();
			
			model.addAttribute("listCountries", countries);
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d)",id));
			
			return "customers/customer_form";
		}catch( Exception e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}
	}
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer,Model model,RedirectAttributes ra) {
		customerService.save(customer);
		ra.addFlashAttribute("message", "The customer ID "+customer.getId()+" has been updated");
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/delete/{id}")
	public String deleteCustomers(@PathVariable Integer id, RedirectAttributes ra) {
		try {
			customerService.delete(id);
			ra.addFlashAttribute("message", "The customer ID "+id+" has been deleted successfully");
		}catch(Exception e) {
			ra.addFlashAttribute("message", e.getCause());
		}
		
		return "redirect:/customers";
	}
}
