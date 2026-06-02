package com.example.forum.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.forum.entity.User;

public class UserDetailsImpls implements UserDetails{
	private final User user;
	private final Collection<? extends GrantedAuthority> authorities;
	
	public UserDetailsImpls(User user) {
		this.user = user;
		
		if(user.getDepartmentId() == 1) {
			//部署IDが1の場合は管理者権限を付与
			this.authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new  SimpleGrantedAuthority("ROLE_USER"));
		} else {
			//その他の部署IDには一般ユーザー権限を付与
			 this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
		}
	}
	
	public User getUser() {
		return user;
	}
	
	@Override
	public String getUsername() {
		return user.getAccount();
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	
	@Override
	public Collection <? extends GrantedAuthority> getAuthorities(){
		return authorities;
	}
	
	@Override
	public boolean isEnabled() {
		if(user.getIsStopped() == 0) {
			return true;
		} else {
			return false;
		}
	}


}
