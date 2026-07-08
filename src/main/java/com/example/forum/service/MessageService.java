package com.example.forum.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forum.controller.form.MessageForm;
import com.example.forum.controller.form.UserMessageForm;
import com.example.forum.entity.Message;
import com.example.forum.entity.User;
import com.example.forum.repository.MessageRepository;

@Service
public class MessageService {
	@Autowired
	MessageRepository messageRepository;
	
	//新規投稿
	public void saveMessage(MessageForm messageForm) {
		Message messageEntity = convetFormToEntity(messageForm);
		messageRepository.save(messageEntity);
	}
	
	//投稿全件検索
	public List<UserMessageForm> findAll(){
		List<Message> allMessages = messageRepository.findAll();
		
		return convertEntityToForm(allMessages);
	}
	
	//投稿IDで検索
	public MessageForm findById(Integer id) {
		Message result = messageRepository.findById(id).get();
		
		return convertEntityToForm(result);
	}
	
	//投稿削除
	public void deleteMessage(Integer id) {
		messageRepository.deleteById(id);
	}
	
	//FormをEntitiyに詰め替える
	private Message convetFormToEntity(MessageForm messageForm) {
		User user = new User();
		user.setId(messageForm.getUserId());
		Message message = new Message();
		message.setTitle(messageForm.getTitle());
		message.setMainText(messageForm.getText());
		message.setCategory(messageForm.getCategory());
		message.setUser(user);
		
		if(messageForm.getId() != null) {
			message.setId(messageForm.getId());
		}
		
		return message;

	}
	
	//EntityをFormに詰め替える
	private MessageForm convertEntityToForm(Message result) {
		MessageForm message = new MessageForm();
		message.setId(result.getId());
		message.setTitle(result.getTitle());
		message.setText(result.getMainText());
		message.setCategory(result.getCategory());
		message.setUserId(result.getUser().getId());
		message.setCreatedDate(result.getCreatedDate());
		message.setUpdatedDate(result.getUpdatedDate());
		
		return message;
	}
	
	//List<Entity>をFormに詰め替える
	private List<UserMessageForm> convertEntityToForm(List<Message> results){
		List<UserMessageForm> messages = new ArrayList<>();
		
		for(Message result : results) {
			UserMessageForm message = new UserMessageForm();
			message.setId(result.getId());
			message.setTitle(result.getTitle());
			message.setText(result.getMainText());
			message.setCategory(result.getCategory());
			message.setUser(result.getUser());
			message.setCreatedDate(result.getCreatedDate());
			message.setUpdatedDate(result.getUpdatedDate());
			messages.add(message);
		}
		
		return messages;
	}
}
