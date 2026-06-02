package com.example.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.forum.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByAccount(String account);
}
