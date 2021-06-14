package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.exception.TokenDoesNotExistException;
import com.renegade.videoondemand.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionApi {
    private final SessionService sessionService;
    private final TokenRepository tokenRepository;

    @PostMapping
    public String retrieveNewToken(User loginCredentials,
                                   @RequestParam("token") String token) {
        if (!tokenRepository.existsById(token)) {
            throw new TokenDoesNotExistException();
        }
        tokenRepository.deleteById(token);
        return sessionService.createNewSession(loginCredentials.getUsername(), loginCredentials.getPassword());
    }

    @DeleteMapping
    public void deleteToken(@RequestHeader("sessionID") String sessionID) {
        sessionService.deleteToken(sessionID);
    }
    
}
