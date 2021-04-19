package com.ytse.youtubestockexchange.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import com.ytse.youtubestockexchange.models.Channel;
import com.ytse.youtubestockexchange.models.Pair;
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
        List<Map<String, Object>> list = new ArrayList<>();
        int cnt = 0;
        while(it.hasNext() && cnt++<LB_SIZE)
            list.add(export(it.next()));
        return ResponseEntity.ok().body(list);
    }

    public ResponseEntity<?> fetchRecentTransactions() {
        return ResponseEntity.ok().body(null);
    }

    public ResponseEntity<?> buyShare(Channel channel) {
        logger.info(authService.currUser.username+" buys share of "+channel);
        Optional<Channel> op = channelRepository.findById(channel.channelId);
        long price = 0;
        if(op.isPresent()) {
            price = op.get().sharePrice;
        }
        else {
            gapiService.setPrice(channel);
            while(channel.sharePrice == 0) {
                try {
                    channel.wait();
                } catch(Exception e) {

                }
            }
            price = channel.sharePrice;
        }
        logger.info("price is "+price);
        User user = authService.currUser;
        logger.info(user.availCash+" "+price);
        if(user.availCash < price)
            return ResponseEntity.status(500).body(Map.of("Message", "Insufficient Cash available"));
        user.availCash -= price;
        user.addHolding(channel.channelId);
        userRepository.save(user);
        if(!channelRepository.findById(channel.channelId).isPresent())
            channelRepository.save(channel);
        return ResponseEntity.ok().body(true);
    }

    public Map<String, Object> export(User user) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", user.username);
        map.put("availCash", user.availCash);
        map.put("netWorth", user.netWorth);
        List<Pair<Channel, Integer>> holdings = new ArrayList<>();
        for(String channelId: user.holdings.keySet())
            holdings.add(new Pair<Channel, Integer>(channelRepository.findById(channelId).get(), user.holdings.get(channelId)));
        map.put("holdings", holdings);
        return map;
    }
}
