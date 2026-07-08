package com.example.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.forum.controller.form.UserMessageForm;
import com.example.forum.security.UserDetailsImpls;
import com.example.forum.service.MessageService;

@Controller
public class HomeController {
	@Autowired
	MessageService messageService;
	//ホーム画面を表示
	@GetMapping("/")
	public String index(@AuthenticationPrincipal UserDetailsImpls loginUser,Model model) {
		//投稿情報取得
		List<UserMessageForm> messages = messageService.findAll();
		
		model.addAttribute("messages", messages);
		model.addAttribute("loginUser", loginUser);
		return "index";
	}
}
