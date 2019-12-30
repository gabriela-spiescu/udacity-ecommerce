package com.gabriela.fabricadefumuri.eCommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriela.fabricadefumuri.eCommerce.entity.User;
import com.gabriela.fabricadefumuri.eCommerce.entity.UserOrder;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
