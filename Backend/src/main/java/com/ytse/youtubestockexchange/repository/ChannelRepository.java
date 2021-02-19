package com.ytse.youtubestockexchange.repository;

import com.ytse.youtubestockexchange.models.Channel;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChannelRepository extends MongoRepository<Channel, String> {
    
}
