package com.ytse.youtubestockexchange;

import com.ytse.youtubestockexchange.repository.UserRepository;
import com.ytse.youtubestockexchange.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YoutubeStockExchangeApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;
	@Autowired
	AuthService authService;
	public static void main(String[] args) {
		SpringApplication.run(YoutubeStockExchangeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userRepository.deleteAll();
	}
}
