package com.katachi.blog.service;

import org.springframework.stereotype.Service;

import com.katachi.blog.model.User;

@Service
public interface UserService {

	User getUserByEmail(String email);

	User register(User user);

}
