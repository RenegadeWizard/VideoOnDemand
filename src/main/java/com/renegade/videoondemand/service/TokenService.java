package com.renegade.videoondemand.service;

import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.repository.UserRepository;
import com.renegade.videoondemand.exception.FailedAuthenticationException;
import com.renegade.videoondemand.exception.TokenDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final Map<String, String> tokens = new HashMap<>();

    public String createNewToken(String username, String password) {
        User user = userRepository.findById(username).orElseThrow(FailedAuthenticationException::new);
        if (!encoder.matches(password, user.getPassword())) {
            throw new FailedAuthenticationException();
        }
        SecureRandom random = new SecureRandom();
        String token = Base64.getEncoder().encodeToString(random.generateSeed(16));
        tokens.put(token, user.getUsername());
        return token;
    }

    public void deleteToken(String token) {
        if (!tokens.containsKey(token)) {
            throw new TokenDoesNotExistException();
        }
        tokens.remove(token);
    }

    public void deleteAllUserTokens(String username) {
        while (tokens.values().remove(username));
    }

    public User getUserByToken(String token) {
        if (!tokens.containsKey(token)) {
            throw new TokenDoesNotExistException();
        }
        return userRepository
                .findById(tokens.get(token))
                .orElseThrow(FailedAuthenticationException::new);
    }
}
