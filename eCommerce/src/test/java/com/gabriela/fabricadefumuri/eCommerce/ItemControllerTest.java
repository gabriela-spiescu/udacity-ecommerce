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

import com.gabriela.fabricadefumuri.eCommerce.controller.ItemController;
import com.gabriela.fabricadefumuri.eCommerce.entity.Item;
import com.gabriela.fabricadefumuri.eCommerce.repository.ItemRepository;

/**
 * @author Gabriela Spiescu
 */
public class ItemControllerTest {
	
	private ItemController itemController;

	private ItemRepository itemRepo = Mockito.mock(ItemRepository.class);
	
	@Before
	public void setUp() {
		itemController = new ItemController();
		TestUtils.injectObject(itemController, "itemRepository", itemRepo);
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
	public void verify_get_items() {
		ResponseEntity<List<Item>> reponse = itemController.getItems();
		List<Item> items = reponse.getBody();
		assertNotNull(reponse);
		assertEquals(true, items.isEmpty());
	}
	
	@Test
	public void verify_get_item_by_id() {
		Item one = getItem(1L);
		itemRepo.save(one);
		Optional<Item> io = Optional.of(one);
		
		System.err.println(itemRepo.count());
		Mockito.when(itemRepo.findById(1L)).thenReturn(io);
		System.err.println(itemRepo.count());
		ResponseEntity<Item> reponse = itemController.getItemById(1L);
		Item i = reponse.getBody();
		assertNotNull(reponse);
		assertEquals("car", i.getName());
	}
	
	@Test
	public void verify_get_items_by_name() {
		List<Item> items = new ArrayList<Item>();
		items.add(getItem(1L));
		items.add(getItem(2L));
		items.add(getItem(3L));
		itemRepo.saveAll(items);
		Mockito.when(itemRepo.findByName("car")).thenReturn(items);
		ResponseEntity<List<Item>> reponse = itemController.getItemsByName("car");
		List<Item> expected = reponse.getBody();
		assertNotNull(reponse);
		assertEquals(3, expected.size());
	}

}
