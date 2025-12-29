package com.pos.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.pos.user.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
    Page<User> findByNameContainingIgnoreCase(String q, Pageable pageable);
}
