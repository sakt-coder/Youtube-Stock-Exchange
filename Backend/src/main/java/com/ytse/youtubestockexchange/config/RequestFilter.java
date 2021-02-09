package com.ytse.youtubestockexchange.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ytse.youtubestockexchange.models.User;
import com.ytse.youtubestockexchange.repository.UserRepository;
import com.ytse.youtubestockexchange.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestFilter extends OncePerRequestFilter {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthService authService;
    
    Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        User user = null;
        if (token != null)
            user = userRepository.findByToken(token);
        
        if (user != null)
            authService.currUser = user;

        filterChain.doFilter(request, response);
    }

}
