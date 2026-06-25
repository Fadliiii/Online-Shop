package com.shopme.site.test.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.site.cart.CartItemRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTest {
	
	@Autowired private CartItemRepository cartItemRepository;
	
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testSaveItem() {
		Integer customerId = 1;
		Integer productId = 1;
		
		Customer customer = entityManager.find(Customer.class, customerId);

		Product product = entityManager.find(Product.class, productId);
		
		CartItem cartItem = new CartItem();
		
		cartItem.setCustomer(customer);
		cartItem.setProduct(product);
		cartItem.setQuantity(1);
		
		CartItem saveItem = cartItemRepository.save(cartItem);
		
		assertThat(saveItem.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testSave2Item() {
		Integer customerId = 10;
		Integer productId = 10;
		
		Customer customer = entityManager.find(Customer.class, customerId);

		Product product = entityManager.find(Product.class, productId);
		
		CartItem Item1 = new CartItem();
		
		Item1.setCustomer(customer);
		Item1.setProduct(product);
		Item1.setQuantity(2);
		
		CartItem item2= new CartItem();
		item2.setCustomer( new Customer(customerId));
		item2.setProduct(new Product(productId));
		item2.setQuantity(3);
		
		Iterable<CartItem> saveItem = cartItemRepository.saveAll(List.of(Item1,item2));
		
		assertThat(saveItem).size().isGreaterThan(0);
	}
	
	@Test
	public void testFindByCustomer() {
		Integer id = 10;
		
		Customer customer = entityManager.find(Customer.class, id);
	
		List<CartItem> cartItems =	cartItemRepository.findByCustomer(customer);
		cartItems.forEach(t ->System.out.println());
		assertThat(cartItems.size()).isEqualTo(2);
		
	}
	
	@Test
	public void testFindByCustomerAndProduct() {
		Integer customerId = 1;
		Integer productId =1;
		
		CartItem item = cartItemRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

		System.out.println(item);
		assertThat(item).isNotNull();
	}

	
	@Test
	public void testUpdateQuantity() {
		Integer productId = 1;
		Integer customerId =1;
		Integer quantity = 4;
		
		cartItemRepository.updateQuantity(quantity, customerId, productId);
		
	    CartItem updatedCartItemQuantity =	cartItemRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
	    
	    assertThat(updatedCartItemQuantity.getQuantity()).isEqualTo(quantity);
	}
	
	@Test
	public void testDeleteByCustomerAndProduct() {
		Integer productId = 10;
		Integer customerId =10;
		
		cartItemRepository.deletedByCustomerAndProduct(customerId, productId);
		CartItem deleted = cartItemRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
		assertThat(deleted).isNull();

	}
}
