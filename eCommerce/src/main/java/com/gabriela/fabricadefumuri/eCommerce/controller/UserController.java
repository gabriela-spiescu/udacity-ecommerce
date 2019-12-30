package com.gabriela.fabricadefumuri.eCommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriela.fabricadefumuri.eCommerce.entity.Cart;
import com.gabriela.fabricadefumuri.eCommerce.entity.User;
import com.gabriela.fabricadefumuri.eCommerce.repository.CartRepository;
import com.gabriela.fabricadefumuri.eCommerce.repository.UserRepository;
import com.gabriela.fabricadefumuri.eCommerce.request.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		Optional<User> opt = userRepository.findById(id);
		if (!opt.isPresent()) {
			log.error("The user with id {} cannot be found", id);
		} else {
			log.info("The user with id {} has the name {}", id, opt.get().getUsername());
		}
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			log.info("The user cannot be found {}", username);
			return ResponseEntity.notFound().build();
		} else {
			log.info("The user {} has {} items with a total sum of {}", username, user.getCart().getItems().size(), user.getCart().getTotal());
			return ResponseEntity.ok(user);
		}
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if (createUserRequest.getPassword().length() < 4 || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			log.error("New user cannot be created because of password is incorrectly initialized {}", createUserRequest.getUsername());
			return ResponseEntity.badRequest().build();
		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		if (userRepository.findByUsername(createUserRequest.getUsername()) != null) {
//			System.err.println("bau bau" + createUserRequest.getUsername());
			log.error("New user cannot be created because there is already an username {} created", createUserRequest.getUsername());
			return ResponseEntity.badRequest().build();
		}
		userRepository.save(user);
		log.info("New user is created: {}", createUserRequest.getUsername());
		return ResponseEntity.ok(user);
	}
	
}
