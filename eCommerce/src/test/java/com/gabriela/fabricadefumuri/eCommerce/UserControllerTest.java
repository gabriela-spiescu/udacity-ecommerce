package com.gabriela.fabricadefumuri.eCommerce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.gabriela.fabricadefumuri.eCommerce.controller.UserController;
import com.gabriela.fabricadefumuri.eCommerce.entity.User;
import com.gabriela.fabricadefumuri.eCommerce.repository.CartRepository;
import com.gabriela.fabricadefumuri.eCommerce.repository.UserRepository;
import com.gabriela.fabricadefumuri.eCommerce.request.CreateUserRequest;

/**
 * @author Gabriela Spiescu
 */
public class UserControllerTest {
	
	private UserController userController;

	private UserRepository userRepo = Mockito.mock(UserRepository.class);
	private CartRepository cartRepo = Mockito.mock(CartRepository.class);
	private BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
	
	@Before
	public void setUp() {
		userController = new UserController();
		TestUtils.injectObject(userController, "userRepository", userRepo);
		TestUtils.injectObject(userController, "cartRepository", cartRepo);
		TestUtils.injectObject(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
	}
	
	@Test
	public void verify_create_user() {
		Mockito.when(bCryptPasswordEncoder.encode("password")).thenReturn("encodedPassword");
		CreateUserRequest userRequest = new CreateUserRequest();
		userRequest.setUsername("first");
		userRequest.setPassword("password");
		userRequest.setConfirmPassword("password");
		
		ResponseEntity<User> reponse = userController.createUser(userRequest);
		assertNotNull(reponse);
		User user = reponse.getBody();
		assertEquals(0, user.getId());
		assertEquals("first", user.getUsername());
		assertEquals("encodedPassword", user.getPassword());
		
		userRequest.setPassword("123");
		ResponseEntity<User> reponse1 = userController.createUser(userRequest);
		assertEquals(400, reponse1.getStatusCodeValue());
		
		userRequest.setPassword("mamaliga");
		userRequest.setConfirmPassword("mamaligi");
		ResponseEntity<User> reponse2 = userController.createUser(userRequest);
		assertEquals(400, reponse2.getStatusCodeValue());
	}
	
//	@Test
	public void verify_findByUserName() {
		User user = new User();
		user.setId(1);
		user.setUsername("myName");
		user.setPassword("password");
		List<User> users = new ArrayList<User>();
		users.add(user);
		Mockito.when(userRepo.findByUsername("myName")).thenReturn(user);
		Mockito.when(userRepo.findByUsername("myName1")).thenReturn(null);
		ResponseEntity<User> reponse = userController.findByUserName("myName");
		assertNotNull(reponse);
		User expected = reponse.getBody();
		assertEquals(1, expected.getId());
		assertEquals("myName", expected.getUsername());
		
		ResponseEntity<User> reponse1 = userController.findByUserName("myName1");
		assertEquals(404, reponse1.getStatusCodeValue());
	}
	
	@Test
	public void verify_findById() {
		User user = new User();
		user.setId(1);
		user.setUsername("myName");
		user.setPassword("password");
		List<User> users = new ArrayList<User>();
		users.add(user);
		Optional<User> uo = Optional.of(user);
		Mockito.when(userRepo.findById(1L)).thenReturn(uo);
		ResponseEntity<User> reponse = userController.findById(1L);
		assertNotNull(reponse);
		User expected = reponse.getBody();
		assertEquals(1, expected.getId());
		assertEquals("myName", expected.getUsername());
	}

}
