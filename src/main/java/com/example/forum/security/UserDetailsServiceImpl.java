package com.example.forum.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.forum.entity.User;
import com.example.forum.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
	    System.out.println("★loadUserByUsernameが呼ばれました。探すアカウント: " + account);
		try {
			User user = userRepository.findByAccount(account);
	        System.out.println("★DBから取得したユーザー: " + user);
			return new UserDetailsImpls(user);
		} catch(Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
		}
	}
}
