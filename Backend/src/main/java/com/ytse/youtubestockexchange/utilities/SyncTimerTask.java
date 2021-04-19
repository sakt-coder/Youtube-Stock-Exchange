package com.ytse.youtubestockexchange.utilities;

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
        List<Channel> channelList = channelRepository.findAll();
        for(Channel channel: channelList)
            gapiService.setPrice(channel);
        for(Channel channel: channelList)
		{
			synchronized(channel) {
			while(channel.sharePrice == 0)
                try {
				    channel.wait();
                } catch(Exception e) {
                    e.printStackTrace();
                }
			}
		}
        channelRepository.saveAll(channelList);

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
        userRepository.saveAll(userList);

        for(User user: userList)
            userService.userSet.add(user);
    }
    
}
