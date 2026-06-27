package com.shopme.site.cart;

import java.util.List;
import com.shopme.site.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;

@Service
public class ShopingCartService {

    private final CustomerRepository customerRepository;
	
	@Autowired private CartItemRepository cartItemRepository;

    ShopingCartService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
	
	public Integer addProduct(Integer prductId,Integer quantity, Customer customer) throws ShoppingCartException {
		Integer updatedQuantity = quantity;
		Product product = new Product(prductId);
		
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);
		
		if(cartItem != null) {
			updatedQuantity = cartItem.getQuantity() + quantity;
			if(updatedQuantity > 5) {
				throw new ShoppingCartException("Could not add "+ quantity + " item(s) because there's already "+ cartItem.getQuantity()+" item(s)"
						+" in your shopping cart. Maximum allowed quantity is 5. ");
			}
		} else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
			
		}
		cartItem.setQuantity(updatedQuantity);
		
		cartItemRepository.save(cartItem);
		return updatedQuantity;
	}
	
	public List<CartItem> findByCustomer (Customer customer){
		return cartItemRepository.findByCustomer(customer);
	}
}
