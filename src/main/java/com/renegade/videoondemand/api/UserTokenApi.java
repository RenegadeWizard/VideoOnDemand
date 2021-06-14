package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.exception.TokenDoesNotExistException;
import com.renegade.videoondemand.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login-tokens")
@RequiredArgsConstructor
public class UserTokenApi {
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;

    @PostMapping
    public String retrieveNewToken(User loginCredentials,
                                   @RequestParam("token") String token) {
        if (!tokenRepository.existsById(token)) {
            throw new TokenDoesNotExistException();
        }
        tokenRepository.deleteById(token);
        return tokenService.createNewToken(loginCredentials.getUsername(), loginCredentials.getPassword());
    }

    @DeleteMapping
    public void deleteToken(@RequestHeader("token") String token) {
        tokenService.deleteToken(token);
    }
    
}
