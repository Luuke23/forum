package com.example.forum.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.forum.controller.form.MessageForm;
import com.example.forum.security.UserDetailsImpls;
import com.example.forum.service.MessageService;

import jakarta.validation.Valid;

@Controller
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    // 新規投稿画面表示
    @GetMapping("/message/new")
    public String newMessage(Model model) {
        // リダイレクト先から送られてきたformModelが既に存在する場合は、それを使う
        if (!model.containsAttribute("formModel")) {
            model.addAttribute("formModel", new MessageForm());
        }
        return "message/new";
    }
    
    // 新規投稿登録処理
    @PostMapping("/message/add")
    public String addMessage(@AuthenticationPrincipal UserDetailsImpls loginUser,
                             @ModelAttribute("formModel") @Valid MessageForm messageForm,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        
        // バリデーションエラーがあった場合、新規投稿画面にリダイレクト
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
            redirectAttributes.addFlashAttribute("formModel", messageForm);
            return "redirect:/message/new";
        }
        
        Integer userId = loginUser.getUser().getId();
        messageForm.setUserId(userId);
    	//LogicService呼び出し
        messageService.saveMessage(messageForm);
        
        return "redirect:/";
    }
    
    //投稿編集画面表示
    @GetMapping("/message/edit/{id}")
    public String editMessage(@PathVariable Integer id,
    						@RequestParam Integer userId,
    						@AuthenticationPrincipal UserDetailsImpls loginUser,
    						RedirectAttributes redirectAttributes,
    						Model model) {
    	
        // リダイレクト先から送られてきたformModelが既に存在する場合は、それを使う
        if (!model.containsAttribute("formModel")) {
            MessageForm messageForm = messageService.findById(id);
            model.addAttribute("formModel", messageForm);
        }
        
    	return "message/edit";
    }
    
    //投稿編集処理
    @PostMapping("/message/update/{id}")
    public String updateMessage(@PathVariable Integer id,
    							@AuthenticationPrincipal UserDetailsImpls loginUser,
            					@ModelAttribute("formModel") @Valid MessageForm messageForm,
            					BindingResult result,
            					RedirectAttributes redirectAttributes) {
        // バリデーションエラーがあった場合、新規投稿画面にリダイレクト
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
            redirectAttributes.addFlashAttribute("formModel", messageForm);
            redirectAttributes.addAttribute("userId", loginUser.getUser().getId());
            return "redirect:/message/edit/" + id;
        }
        
        messageForm.setId(id);
    	//LogicService呼び出し
        messageService.saveMessage(messageForm);
        
    	return "redirect:/";
    }
    
    //投稿削除処理
    @PostMapping("/message/delete/{id}")
    public String deleteMessage(@PathVariable Integer id, @RequestParam Integer userId, @AuthenticationPrincipal UserDetailsImpls loginUser, RedirectAttributes redirectAttributes) {
    	
    	Integer loginUserId = loginUser.getUser().getId();
    	
    	//削除権限チェック
    	if(!Objects.equals(loginUserId, userId)) {
    		String errorMessage = "無効なアクセスです。";
    		redirectAttributes.addFlashAttribute(errorMessage);
    		return "redirect:/";
    	}
    	
    	//LogicService呼び出し
    	messageService.deleteMessage(id);
    	return "redirect:/";
    }
}