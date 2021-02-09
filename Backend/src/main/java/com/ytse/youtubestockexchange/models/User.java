package com.ytse.youtubestockexchange.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ytse.youtubestockexchange.constants.YtseConstants;

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
    public List<Holding> holdings;
    public int availCash;
    public int netWorth;

    public User(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.availCash = YtseConstants.OPENING_BALANCE;
        this.netWorth = YtseConstants.OPENING_BALANCE;
        this.holdings = new ArrayList<Holding>();
    }
}
