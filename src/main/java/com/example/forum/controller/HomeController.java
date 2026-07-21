package com.example.forum.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.forum.controller.form.UserMessageForm;
import com.example.forum.security.UserDetailsImpls;
import com.example.forum.service.MessageService;

@Controller
public class HomeController {
	@Autowired
	MessageService messageService;
	//ホーム画面を表示
	@GetMapping("/")
	public String index(@AuthenticationPrincipal UserDetailsImpls loginUser,
						Model model,
						@RequestParam(name = "keyword", required = false) String keyword, 
						@RequestParam(name = "category", required = false) String category) {
		//メッセージリストの初期化
		List<UserMessageForm> messages = new ArrayList<>();
		String returnKeyword = "";
		
		if((keyword != null)&&(!keyword.isEmpty())){
			//投稿情報キーワード検索
			messages = messageService.findByKeyWord(keyword);
			returnKeyword = keyword;
		} else if((category != null)&&(!category.isEmpty())){
			messages = messageService.findByCategory(category);
		}else {
			//投稿情報全件取得
			messages = messageService.findAll();
		}

		model.addAttribute("keyword", returnKeyword);
		model.addAttribute("messages", messages);
		model.addAttribute("loginUser", loginUser);
		return "index";
	}
}
