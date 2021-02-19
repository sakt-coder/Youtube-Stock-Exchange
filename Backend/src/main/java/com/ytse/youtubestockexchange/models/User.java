package com.ytse.youtubestockexchange.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import static com.ytse.youtubestockexchange.constants.YtseConstants.OPENING_BALANCE;

import org.springframework.data.annotation.Id;

public class User {
    @Id
    @JsonIgnore
    public String id;
    public String username;
    @JsonIgnore
    public String password;
    @JsonIgnore
    public String token;
    public Map<String, Integer> holdings;
    public long availCash;
    public long netWorth;

    public User(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.availCash = OPENING_BALANCE;
        this.netWorth = OPENING_BALANCE;
        this.holdings = new HashMap<String, Integer>();
    }

    public void addHolding(String channelId) {
        this.holdings.put(channelId, this.holdings.getOrDefault(channelId, 0) + 1);
    }

    public String toString() {
        return username+" "+availCash+" "+netWorth+" "+holdings.toString();
    }
}
