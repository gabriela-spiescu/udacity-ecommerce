package com.gabriela.fabricadefumuri.eCommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriela.fabricadefumuri.eCommerce.entity.Cart;
import com.gabriela.fabricadefumuri.eCommerce.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
