package com.shopme.site.cart;

import java.net.http.HttpClient;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.webjars.NotFoundException;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.site.customer.CustomerService;
import com.shopme.site.utill.Utility;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CartController {

	@Autowired private ShopingCartService cartService;
	@Autowired private CustomerService customerService;

	@GetMapping("/cart")	
	public String viewCart(HttpServletRequest request,Model model) {

		Customer customer = getAuthenticatedCustomer(request);
		
		List<CartItem> cartItems = cartService.findByCustomer(customer);
		
		float estimatedTotal = 0.0f;
		
		for(CartItem item : cartItems) {
			estimatedTotal += item.getSubtotal();
		}
		
		model.addAttribute("estimatedTotal", estimatedTotal);
		model.addAttribute("cartItems", cartItems);
		return "/cart/shopping_cart";
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailAuthenticatedCustomer(request);
		if (email == null) {
			throw new NotFoundException("No authenticated customer");
		}
		return customerService.getCustomerByEmail(email);
	}
	
	
}
