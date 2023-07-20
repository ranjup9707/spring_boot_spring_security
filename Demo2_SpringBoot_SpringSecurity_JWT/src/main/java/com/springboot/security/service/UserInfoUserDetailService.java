package com.springboot.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.springboot.security.config.UserInfoUserDetails;
import com.springboot.security.entity.UserInfo;
import com.springboot.security.repository.UserInfoRepository;

public class UserInfoUserDetailService implements UserDetailsService 
{
	
	@Autowired
	private UserInfoRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfo> userInfo = repo.findByName(username);
		return userInfo.map(UserInfoUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

}
