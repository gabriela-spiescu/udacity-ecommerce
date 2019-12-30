package com.gabriela.fabricadefumuri.eCommerce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import com.gabriela.fabricadefumuri.eCommerce.controller.OrderController;
import com.gabriela.fabricadefumuri.eCommerce.entity.Cart;
import com.gabriela.fabricadefumuri.eCommerce.entity.Item;
import com.gabriela.fabricadefumuri.eCommerce.entity.User;
import com.gabriela.fabricadefumuri.eCommerce.entity.UserOrder;
import com.gabriela.fabricadefumuri.eCommerce.repository.OrderRepository;
import com.gabriela.fabricadefumuri.eCommerce.repository.UserRepository;

/**
 * @author Gabriela Spiescu
 */
public class OrderControllerTest {
	
	private OrderController orderController;

	private UserRepository userRepo = Mockito.mock(UserRepository.class);
	private OrderRepository orderRepo = Mockito.mock(OrderRepository.class);
	
	@Before
	public void setUp() {
		orderController = new OrderController();
		TestUtils.injectObject(orderController, "userRepository", userRepo);
		TestUtils.injectObject(orderController, "orderRepository", orderRepo);
	}
	
	private User getUser() {
		User user = new User();
		user.setId(1);
		user.setUsername("myName");
		user.setPassword("password");
		List<User> users = new ArrayList<User>();
		users.add(user);
		
		Cart cart = new Cart();
		cart.setId(1L);
		cart.setUser(user);
		cart.setTotal(BigDecimal.ZERO);
		cart.setItems(new ArrayList<Item>());
		user.setCart(cart);
		return user;
	}

	@Test
	public void verify_submit_order() {
		User user = getUser();
		Mockito.when(userRepo.findByUsername("myName")).thenReturn(user);
		ResponseEntity<UserOrder> reponse = orderController.submit("myName");
		assertNotNull(reponse);
		UserOrder expected = reponse.getBody();
		assertEquals(BigDecimal.ZERO, expected.getTotal());
		assertEquals("myName", expected.getUser().getUsername());
	}
	
	@Test
	public void verify_get_orders_for_user() {
		User user = getUser();
		UserOrder order = UserOrder.createFromCart(user.getCart());
		List<UserOrder> orders = new ArrayList<UserOrder>();
		orders.add(order);
		System.err.println("order" + order.getUser().getUsername());
		Mockito.when(userRepo.findByUsername("myName")).thenReturn(user);
		Mockito.when(orderRepo.findByUser(user)).thenReturn(orders);
		ResponseEntity<List<UserOrder>> reponse = orderController.getOrdersForUser("myName");
		assertNotNull(reponse);
		List<UserOrder> expected = reponse.getBody();
		assertEquals(1, expected.size());
	}
	
}
