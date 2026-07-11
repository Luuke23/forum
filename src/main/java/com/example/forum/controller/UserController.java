package com.example.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.forum.controller.form.UserForm;
import com.example.forum.security.UserDetailsImpls;
import com.example.forum.service.UserService;
import com.example.forum.validation.CreateGroup;

import jakarta.validation.groups.Default;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	
	//新規ユーザー登録画面表示
	@GetMapping("/user/new")
	public String newUser(@AuthenticationPrincipal UserDetailsImpls loginUser,Model model) {
		
		if(!model.containsAttribute("formModel")) {
			UserForm userForm = new UserForm();
			model.addAttribute("formModel", userForm);
		}
		
		model.addAttribute("loginUser", loginUser);
		
		return "user/new";
	}
	
	//新規ユーザー登録
	@PostMapping("/user/add")
	public String addUser(@AuthenticationPrincipal UserDetailsImpls loginUser,
            @ModelAttribute("formModel") @Validated({Default.class, CreateGroup.class}) UserForm userForm,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            @RequestParam String confirmationPassword,
            Model model) {
		
		if((userForm.getPassword() != null) && (!userForm.getPassword().equals(confirmationPassword))) {
            FieldError fieldError = new FieldError(result.getObjectName(),
                    "password", "パスワードと確認用パスワードが一致しません");
            result.addError(fieldError);
		}
		
		if(result.hasErrors()) {
			model.addAttribute("formModel", userForm);
			model.addAttribute("loginUser", loginUser);
			return "user/new";
		}
		
		userService.saveUser(userForm);
		
		return "redirect:/";
		
	}
	
	//ユーザー編集画面表示
	@GetMapping("/user/edit")
	public String editUser(@AuthenticationPrincipal UserDetailsImpls loginUser,Model model) {
		
		if(!model.containsAttribute("formModel")) {
			UserForm userForm =  userService.selectUserById(loginUser.getUser().getId());
			
			model.addAttribute("formModel", userForm);
		}
		
		return "user/edit";
	}
	
	//ユーザー編集
	@PostMapping("/user/update")
	public String updateUser(@AuthenticationPrincipal UserDetailsImpls loginUser, 
							@ModelAttribute("formModel") @Validated({Default.class}) UserForm userForm,
							BindingResult result,
							RedirectAttributes redirectAttributes,
							@RequestParam String confirmationPassword,
							Model model) {
		
		if((userForm.getPassword() != null) && (!userForm.getPassword().equals(confirmationPassword))) {
            FieldError fieldError = new FieldError(result.getObjectName(),
                    "password", "パスワードと確認用パスワードが一致しません");
            result.addError(fieldError);
		}
		
		if(result.hasErrors()) {
			model.addAttribute("formModel", userForm);
			model.addAttribute("loginUser", loginUser);
			return "user/edit";
		}
		
		userForm.setId(loginUser.getUser().getId());
		
		userService.saveUser(userForm);
		
		return "redirect:/";
	}
}
