package com.example.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.forum.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

}
