package com.example.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.forum.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
	@Query("SELECT m FROM Message m " + 
			"WHERE m.mainText LIKE %:keyword%")
	public List<Message> findByMainTextLike(@Param("keyword") String keyword);

	public List<Message> findByCategoryLike(String category);
}
