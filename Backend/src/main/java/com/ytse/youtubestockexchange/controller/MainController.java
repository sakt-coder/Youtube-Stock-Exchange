package com.ytse.youtubestockexchange.controller;

import com.ytse.youtubestockexchange.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin
public class MainController {

    @Autowired
    AuthService authService;

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        logger.info("Logging in with username = "+username+", password = "+password);
        return authService.login(username, password);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("username") String username, @RequestParam("password") String password) {
        logger.info("Registering with username = "+username+", password = "+password);
        return authService.register(username, password);
    }

    @GetMapping("/fetchLeaderBoard")
    public ResponseEntity<?> fetchLeaderBoard() {
        logger.info("Fetching LeaderBoard");
        return null;
    }

    @GetMapping("/fetchRecentTransactions")
    public ResponseEntity<?> fetchRecentTransactions() {
        logger.info("Fetching Recent Transactions");
        return null;
    }

    @PostMapping("/buyShare")
    public ResponseEntity<?> buyShare(@RequestParam("channelId") String channelId) {
        logger.info(authService.currUser.username+" buying share of "+channelId);
        return null;
    }

    @GetMapping("/searchChannel")
    public ResponseEntity<?> search(@RequestParam("query") String query) {
        logger.info(authService.currUser.username+" searching for "+query);
        return null;
    }
}