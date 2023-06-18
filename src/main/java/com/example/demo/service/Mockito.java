package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.entity.User;

@Service
public class Mockito {
	 
	@Autowired
	private UserRepository userDao;
	
	private User getUser(String account) {
		User user = userDao.findUserByAccount(account);
		//System.out.println(user.getAccount());
		return user; 
	}
	
	public User get(String string){
		User user = getUser(string);
		return user;
	}
}
