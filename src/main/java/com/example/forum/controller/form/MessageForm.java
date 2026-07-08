package com.example.forum.controller.form;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageForm {
	
	//ID
	private Integer id;
	
	//タイトル
	@NotBlank(message = "タイトルを入力してください")
	private String title;
	
	//本文
	@NotBlank(message = "本文を入力してください")
	private String text;
	
	@NotBlank(message = "カテゴリを入力してください")
	private String category;
	
	private Integer userId;
	
	private Timestamp createdDate;
	
	private Timestamp updatedDate;

}
