package com.ytse.youtubestockexchange.repository;

import com.ytse.youtubestockexchange.models.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    public User findByUsername(String username);
    public User findByToken(String token);
}
