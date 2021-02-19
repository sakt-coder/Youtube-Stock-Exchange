package com.ytse.youtubestockexchange.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import com.ytse.youtubestockexchange.models.Channel;
import com.ytse.youtubestockexchange.models.User;
import com.ytse.youtubestockexchange.repository.ChannelRepository;
import com.ytse.youtubestockexchange.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthService authService;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    GAPIService gapiService;

    Logger logger;

    public SortedSet<User> userSet;
    private final int LB_SIZE = 5;

    UserService() {
        logger = LoggerFactory.getLogger(UserService.class);
        userSet = new TreeSet<User>((p, q)-> Long.compare(p.netWorth,q.netWorth));
    }

    public ResponseEntity<?> fetchLeaderBoard() {
        Iterator<User> it = userSet.iterator();
        User ret[] = new User[Math.min(LB_SIZE, userSet.size())];
        int cnt = 0;
        while(it.hasNext() && cnt<LB_SIZE) {
            ret[cnt] = it.next();
            cnt++;
        }
        return ResponseEntity.ok().body(ret);
    }

    public ResponseEntity<?> buyShare(String channelId) {
        logger.info(authService.currUser.username+" buys share of "+channelId);
        Optional<Channel> op = channelRepository.findById(channelId);
        long price = 0;
        if(op.isPresent()) {
            price = op.get().sharePrice;
        }
        else {
            price = gapiService.getPrices(new ArrayList<>(){{
                add(channelId);
            }}).get(0);
        }
        logger.info("price is "+price);
        User user = authService.currUser;
        logger.info(user.availCash+" "+price);
        if(user.availCash < price)
            return ResponseEntity.status(500).body(Map.of("Message", "Insufficient Cash available"));
        user.availCash -= price;
        user.addHolding(channelId);
        userRepository.save(user);
        if(!channelRepository.findById(channelId).isPresent())
            channelRepository.save(new Channel("unknown", channelId));
        return ResponseEntity.ok().body(true);
    }
}
