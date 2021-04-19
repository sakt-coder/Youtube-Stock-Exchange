package com.ytse.youtubestockexchange.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.ytse.youtubestockexchange.models.Channel;
import com.ytse.youtubestockexchange.service.GAPIService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BufferTimerTask extends TimerTask {

    Logger logger = LoggerFactory.getLogger(BufferTimerTask.class);

    @Autowired
    GAPIService gapiService;

    @Override
    public void run() {
        if(gapiService.buffer.size() == 0)
            return;
        logger.info("Buffer processing");
        ArrayList<Channel> list = new ArrayList<>();
        ArrayList<String> queryList = new ArrayList<>();
        while(!gapiService.buffer.isEmpty()) {
            Channel channel = gapiService.buffer.remove();
            list.add(channel);
            queryList.add(channel.channelId);
        }
        List<Long> result = gapiService.getPrices(queryList);
        for(int i=0;i<list.size();i++)
        {
            list.get(i).sharePrice = result.get(i);
            gapiService.cache.put(list.get(i).channelId, list.get(i).sharePrice);
            synchronized(list.get(i)) {
                list.get(i).notify();
            }
        }
    }
    
}
