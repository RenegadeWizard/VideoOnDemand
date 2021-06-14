package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.PostToken;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequestMapping("/post-tokens")
@RequiredArgsConstructor
public class PostTokenApi {
    private final TokenRepository tokenRepository;

    @PostMapping
    public PostToken createToken() {
        SecureRandom random = new SecureRandom();
        String tokenValue = Base64.getEncoder().encodeToString(random.generateSeed(16)).replaceAll("\\+", "#").replaceAll("/", "\\$");
        PostToken token = new PostToken(tokenValue);
        tokenRepository.save(token);
        return token;
    }
}
