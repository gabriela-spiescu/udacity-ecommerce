package com.gabriela.fabricadefumuri.eCommerce.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriela.fabricadefumuri.eCommerce.entity.Item;
import com.gabriela.fabricadefumuri.eCommerce.repository.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {
	
	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		List<Item> items = itemRepository.findAll();
		log.info("The number of items is", items.size());
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		Optional<Item> item = itemRepository.findById(id);
		if (item.isPresent()) {
			log.info("The founded item name is", item.get().getName());
			return ResponseEntity.of(itemRepository.findById(id));
		}
		log.error("The founded item is", item.get());
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		if (items == null || items.isEmpty() ) {
			log.error("No item founded with the name", name);
			return ResponseEntity.notFound().build();
		}
		log.info("Number of founded items is ", items.size());
		return ResponseEntity.ok(items);
			
	}
	
}
