package com.ytse.youtubestockexchange;

import java.util.Date;
import java.util.Timer;

import com.ytse.youtubestockexchange.repository.ChannelRepository;
import com.ytse.youtubestockexchange.repository.UserRepository;
import com.ytse.youtubestockexchange.service.AuthService;
import com.ytse.youtubestockexchange.service.GAPIService;
import com.ytse.youtubestockexchange.service.UserService;
import com.ytse.youtubestockexchange.utilities.SyncTimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YoutubeStockExchangeApplication implements CommandLineRunner{

	private static final long REFRESH_RATE = 10000L;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AuthService authService;
	@Autowired
	GAPIService gapiService;
	@Autowired
	UserService userService;
	@Autowired
	ChannelRepository channelRepository;
	@Autowired
	SyncTimerTask task;

	Logger logger = LoggerFactory.getLogger(YoutubeStockExchangeApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(YoutubeStockExchangeApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		Timer timer = new Timer("Sync Timer");
		timer.scheduleAtFixedRate(task, new Date(), REFRESH_RATE);
	}
}
