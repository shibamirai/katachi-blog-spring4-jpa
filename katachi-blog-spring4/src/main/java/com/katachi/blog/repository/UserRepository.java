package com.katachi.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.katachi.blog.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
