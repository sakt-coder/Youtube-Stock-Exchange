package com.ytse.youtubestockexchange.controller;

import com.ytse.youtubestockexchange.models.Channel;
import com.ytse.youtubestockexchange.service.AuthService;
import com.ytse.youtubestockexchange.service.GAPIService;
import com.ytse.youtubestockexchange.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin
public class MainController {

    @Autowired
    AuthService authService;
    @Autowired
    GAPIService gapiService;
    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        logger.info("Logging in with username = "+username+", password = "+password);
        return authService.login(username, password);
    }

    @GetMapping("/autoLogin")
    public ResponseEntity<?> autoLogin() {
        logger.info("AutoLogin");
        return authService.autoLogin();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        logger.info("logging out" + authService.currUser==null?"null":authService.currUser.username);
        return authService.logout();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("username") String username, @RequestParam("password") String password) {
        logger.info("Registering with username = "+username+", password = "+password);
        return authService.register(username, password);
    }

    @GetMapping("/fetchLeaderBoard")
    public ResponseEntity<?> fetchLeaderBoard() {
        logger.info("Fetching LeaderBoard");
        return userService.fetchLeaderBoard();
    }

    @GetMapping("/fetchRecentTransactions")
    public ResponseEntity<?> fetchRecentTransactions() {
        logger.info("Fetching Recent Transactions");
        return userService.fetchRecentTransactions();
    }

    @PostMapping("/buyShare")
    public ResponseEntity<?> buyShare(@RequestBody Channel channel) {
        logger.info(authService.currUser.username+" buying share of "+channel);
        return userService.buyShare(channel);
    }

    @GetMapping("/searchChannel")
    public ResponseEntity<?> search(@RequestParam("query") String query) {
        logger.info(authService.currUser.username+" searching for "+query);
        return gapiService.search(query);
    }
}