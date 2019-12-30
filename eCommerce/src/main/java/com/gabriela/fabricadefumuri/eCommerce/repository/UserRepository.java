package com.gabriela.fabricadefumuri.eCommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriela.fabricadefumuri.eCommerce.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
