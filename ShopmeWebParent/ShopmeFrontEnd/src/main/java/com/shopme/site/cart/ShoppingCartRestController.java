package com.shopme.site.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import com.shopme.common.entity.Customer;
import com.shopme.site.customer.CustomerService;
import com.shopme.site.utill.Utility;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {

	@Autowired private ShopingCartService cartService;
	@Autowired private CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable(name = "productId") Integer productId,
			@PathVariable("quantity")Integer quantity,HttpServletRequest request) throws ShoppingCartException {
		try{
			Customer customer = getAuthenticatedCustomer(request);
		Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);
		
		return updatedQuantity + " item(s) of this product were added to your shopping cart.";
		}catch (NotFoundException e) {
			return "You must login to add this product to cart.";
		}catch(ShoppingCartException ex) {
			return ex.getMessage();
		}
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailAuthenticatedCustomer(request);
		if (email == null) {
			throw new NotFoundException("No authenticated customer");
		}
		return customerService.getCustomerByEmail(email);
	}
}
