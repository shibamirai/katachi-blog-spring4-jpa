package com.katachi.blog.service;

import com.katachi.blog.model.User;

public interface UserService {
	User getUserByEmail(String email);
}
