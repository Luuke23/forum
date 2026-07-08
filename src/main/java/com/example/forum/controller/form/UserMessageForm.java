package com.example.forum.controller.form;

import java.sql.Timestamp;

import com.example.forum.entity.User;

import lombok.Data;

@Data
public class UserMessageForm {
    private Integer id;
    private String title;
    private String text;
    private String category;
    private User user;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
