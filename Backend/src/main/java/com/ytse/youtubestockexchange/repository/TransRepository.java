package com.ytse.youtubestockexchange.repository;

import com.ytse.youtubestockexchange.models.Transaction;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransRepository extends MongoRepository<Transaction, String> {
    
}
