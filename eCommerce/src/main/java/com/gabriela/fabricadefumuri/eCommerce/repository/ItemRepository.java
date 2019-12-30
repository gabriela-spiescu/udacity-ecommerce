package com.gabriela.fabricadefumuri.eCommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriela.fabricadefumuri.eCommerce.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	List<Item> findByName(String name);

}
