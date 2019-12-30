package com.gabriela.fabricadefumuri.eCommerce.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriela.fabricadefumuri.eCommerce.entity.User;
import com.gabriela.fabricadefumuri.eCommerce.entity.UserOrder;
import com.gabriela.fabricadefumuri.eCommerce.repository.OrderRepository;
import com.gabriela.fabricadefumuri.eCommerce.repository.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("User with name {} cannot be found", username);
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		log.info("The user {} submitted {} items from the {} order", username, order.getItems().size(), order.getId());
		orderRepository.save(order);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("User with name {} cannot be found", username);
			return ResponseEntity.notFound().build();
		}
		List<UserOrder> orders = orderRepository.findByUser(user);
		log.info("The user {} has {} orders", username, orders.size());
		return ResponseEntity.ok(orders);
	}
}
