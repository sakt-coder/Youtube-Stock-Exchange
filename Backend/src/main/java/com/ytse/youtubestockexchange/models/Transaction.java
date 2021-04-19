package com.ytse.youtubestockexchange.models;

import java.util.Date;

public class Transaction {
    
    public String message;
    public Date date;

    Transaction(String message) {
        this.message = message;
        this.date = new Date();
    }
}
