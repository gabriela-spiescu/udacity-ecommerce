package com.gabriela.fabricadefumuri.eCommerce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import com.gabriela.fabricadefumuri.eCommerce.controller.CartController;
import com.gabriela.fabricadefumuri.eCommerce.entity.Cart;
import com.gabriela.fabricadefumuri.eCommerce.entity.Item;
import com.gabriela.fabricadefumuri.eCommerce.entity.User;
import com.gabriela.fabricadefumuri.eCommerce.repository.CartRepository;
import com.gabriela.fabricadefumuri.eCommerce.repository.ItemRepository;
import com.gabriela.fabricadefumuri.eCommerce.repository.UserRepository;
import com.gabriela.fabricadefumuri.eCommerce.request.ModifyCartRequest;

/**
 * @author Gabriela Spiescu
 */
public class CartControllerTest {
	
	private CartController cartController;

	private UserRepository userRepo = Mockito.mock(UserRepository.class);
	private CartRepository cartRepo = Mockito.mock(CartRepository.class);
	private ItemRepository itemRepo = Mockito.mock(ItemRepository.class);
	
	@Before
	public void setUp() {
		cartController = new CartController();
		TestUtils.injectObject(cartController, "userRepository", userRepo);
		TestUtils.injectObject(cartController, "cartRepository", cartRepo);
		TestUtils.injectObject(cartController, "itemRepository", itemRepo);
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
	
	private Item getItem(Long id) {
		Item item = new Item();
		item.setId(id);
		item.setName("car");
		item.setPrice(BigDecimal.TEN);
		item.setDescription("a nice electric car");
		return item;
	}
	
	@Test
	public void verify_addToCart() {
		ModifyCartRequest cart = new ModifyCartRequest();
		User my = getUser();
		Item item = getItem(1L);
		cart.setItemId(item.getId());
		cart.setQuantity(2);
		cart.setUsername(my.getUsername());
		Mockito.when(userRepo.findByUsername("myName")).thenReturn(my);
		Optional<Item> io = Optional.of(item);
		Mockito.when(itemRepo.findById(1L)).thenReturn(io);
		ResponseEntity<Cart> reponse = cartController.addTocart(cart);
		Cart expected = reponse.getBody();
		assertNotNull(reponse);
		assertEquals("myName", expected.getUser().getUsername());
		assertEquals(2, expected.getItems().size());
	}
	
	@Test
	public void verify_removeToCart() {
		ModifyCartRequest cart = new ModifyCartRequest();
		User my = getUser();
		Item item = getItem(1L);
		cart.setItemId(item.getId());
		cart.setQuantity(5);
		cart.setUsername(my.getUsername());
		Mockito.when(userRepo.findByUsername("myName")).thenReturn(my);
		Optional<Item> io = Optional.of(item);
		Mockito.when(itemRepo.findById(1L)).thenReturn(io);
		cartController.addTocart(cart);
		cart.setQuantity(1);
		ResponseEntity<Cart> reponse = cartController.removeFromcart(cart);
		Cart expected = reponse.getBody();
		assertNotNull(reponse);
		assertEquals("myName", expected.getUser().getUsername());
		assertEquals(4, expected.getItems().size());
	}

}
