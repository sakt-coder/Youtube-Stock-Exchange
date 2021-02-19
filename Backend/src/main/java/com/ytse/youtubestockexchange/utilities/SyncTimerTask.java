package com.ytse.youtubestockexchange.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import com.ytse.youtubestockexchange.models.Channel;
import com.ytse.youtubestockexchange.models.User;
import com.ytse.youtubestockexchange.repository.ChannelRepository;
import com.ytse.youtubestockexchange.repository.UserRepository;
import com.ytse.youtubestockexchange.service.GAPIService;
import com.ytse.youtubestockexchange.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyncTimerTask extends TimerTask {

    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    GAPIService gapiService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(SyncTimerTask.class);

    @Override
    public void run() {
        logger.info("Database syncing");

        List<Channel> channelList = channelRepository.findAll();
        logger.info(channelList.toString());
        List<String> queryList = new ArrayList<>();
        for(Channel channel: channelList)
            queryList.add(channel.channelId);
        List<Long> prices = gapiService.getPrices(queryList);
        if(prices != null) {
            logger.info(prices.toString());
            for(int i=0;i<channelList.size();i++)
                channelList.get(i).sharePrice = prices.get(i);
            channelRepository.saveAll(channelList);
        }

        Map<String, Long> priceMap = new HashMap<>();
        for(Channel channel: channelList)
            priceMap.put(channel.channelId, channel.sharePrice);

        List<User> userList = userRepository.findAll();
        for(User user: userList)
        {
            long netWorth = user.availCash;
            for(String channelId: user.holdings.keySet())
                netWorth += user.holdings.get(channelId) * priceMap.getOrDefault(channelId, 0l);
            user.netWorth = netWorth;
        }
        logger.info(userList.toString());
        userRepository.saveAll(userList);

        for(User user: userList)
            userService.userSet.add(user);
    }
    
}
