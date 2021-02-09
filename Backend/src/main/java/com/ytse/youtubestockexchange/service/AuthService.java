package com.ytse.youtubestockexchange.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

import com.ytse.youtubestockexchange.models.User;
import com.ytse.youtubestockexchange.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public User currUser;

    public ResponseEntity<?> login(String username, String password) {
        User user;
        try {
            user =  userRepository.findByUsername(username);
        }
        catch(Exception e){
            return ResponseEntity.status(401).body(Map.of("Message", "Login Failed"));
        }
        if(user == null)
            return ResponseEntity.status(401).body(Map.of("Message", "No Such Username exists"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", user.token);
        headers.set("Access-Control-Expose-Headers", "Token");

        return ResponseEntity.ok().headers(headers).body(user);
    }

    public ResponseEntity<?> register(String username, String password) {

        String token = generateNewToken();
        User newUser = new User(username, password, token);
        userRepository.save(newUser);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.set("Access-Control-Expose-Headers", "Token");
        
        return ResponseEntity.ok().headers(headers).body(newUser);
    }
    
    public String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
