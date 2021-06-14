package com.renegade.videoondemand.service;

import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.repository.UserRepository;
import com.renegade.videoondemand.exception.FailedAuthenticationException;
import com.renegade.videoondemand.exception.SessionDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final Map<String, String> sessions = new HashMap<>();

    public String createNewSession(String username, String password) {
        User user = userRepository.findById(username).orElseThrow(FailedAuthenticationException::new);
        if (!encoder.matches(password, user.getPassword())) {
            throw new FailedAuthenticationException();
        }
        SecureRandom random = new SecureRandom();
        String sessionID = Base64.getEncoder().encodeToString(random.generateSeed(16));
        sessions.put(sessionID, user.getUsername());
        return sessionID;
    }

    public void deleteToken(String sessionID) {
        if (!sessions.containsKey(sessionID)) {
            throw new SessionDoesNotExistException();
        }
        sessions.remove(sessionID);
    }

    public void deleteAllUserTokens(String username) {
        while (sessions.values().remove(username));
    }

    public User getUserByToken(String sessionID) {
        if (!sessions.containsKey(sessionID)) {
            throw new SessionDoesNotExistException();
        }
        return userRepository
                .findById(sessions.get(sessionID))
                .orElseThrow(FailedAuthenticationException::new);
    }
}
