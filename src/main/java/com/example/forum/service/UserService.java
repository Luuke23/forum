package com.example.forum.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.forum.controller.form.UserForm;
import com.example.forum.entity.User;
import com.example.forum.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
    @Autowired
    PasswordEncoder passwordEncoder;
    
    //ユーザー登録・更新
    public void saveUser(UserForm userForm) {
        boolean isNewUser = userForm.getId() == null;
        boolean changeIsStopped = false;

        User dbUser = null;
        if (!isNewUser) {
            dbUser = userRepository.findById(userForm.getId()).orElse(null);
        }
        // ユーザー停止状態変更有無判定
        if (dbUser != null && dbUser.getIsStopped() != userForm.getIsStopped()) {
            changeIsStopped = true;
        }

        // 新規ユーザー または ユーザー更新で新規パスワード入力ありの場合、パスワードを暗号化
        if (isNewUser || !userForm.getPassword().isBlank() && !changeIsStopped) {
            String rawPassword = userForm.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            userForm.setPassword(encodedPassword);
        } else {
            User user = userRepository.findById(userForm.getId()).orElse(null);
            userForm.setPassword(user.getPassword());
        }

        User saveUser = setUserEntity(userForm);
        userRepository.save(saveUser);
    }
    
    //IDでユーザー情報を取得
	public UserForm selectUserById(Integer id) {
    	User user =  userRepository.getById(id);
    	UserForm userForm = setUserForm(user);
    	
    	return userForm;
    }
    
    //Formの中身をEntityに詰め替え
    private User setUserEntity(UserForm reqUser) {
        User user = new User();
        user.setAccount(reqUser.getAccount());
        user.setPassword(reqUser.getPassword());
        user.setName(reqUser.getName());
        user.setBranchId(reqUser.getBranchId());
        user.setDepartmentId(reqUser.getDepartmentId());
        user.setIsStopped(reqUser.getIsStopped());

        if (reqUser.getId() != null) {
            user.setId(reqUser.getId());
            user.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        return user;
    }
    
    //Entityの中身をFormに詰め替え
    private UserForm setUserForm(User user) {
    	UserForm userForm = new UserForm();
    	userForm.setId(user.getId());
    	userForm.setAccount(user.getAccount());
    	userForm.setName(user.getName());
    	userForm.setBranchId(user.getBranchId());
    	userForm.setDepartmentId(user.getDepartmentId());
    	userForm.setPassword(user.getPassword());
    	userForm.setIsStopped(user.getIsStopped());
    	
    	return userForm;
    }
}
