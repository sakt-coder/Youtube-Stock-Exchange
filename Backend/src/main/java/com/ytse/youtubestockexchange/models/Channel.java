package com.ytse.youtubestockexchange.models;

import org.springframework.data.annotation.Id;

public class Channel {
    
    @Id
    public String channelId;
    public String channelName;
    public long sharePrice;

    public Channel(String channelName, String channelId) {
        this.channelName = channelName;
        this.channelId = channelId;
        this.sharePrice = 0;
    }

    public String toString() {
        return this.channelName+" "+this.channelId+" "+this.sharePrice;
    }
}
