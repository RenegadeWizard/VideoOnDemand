package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Token;
import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.domain.repository.UserRepository;
import com.renegade.videoondemand.exception.FailedAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class TokenApi {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping
    public String retrieveNewToken(User loginCredentials) {
        SecureRandom random = new SecureRandom();
        Optional<User> optionalUser = userRepository.findById(loginCredentials.getUsername());
        User currentUser = optionalUser.orElseThrow(FailedAuthenticationException::new);
        if (!encoder.matches(loginCredentials.getPassword(), currentUser.getPassword())) {
            throw new FailedAuthenticationException();
        }
        Token token = new Token(Base64.getEncoder().encodeToString(random.generateSeed(16)), currentUser);
        tokenRepository.save(token);
        return token.getValue();
    }

    @DeleteMapping
    public void deleteToken(@RequestHeader("token") String token) {
        tokenRepository.deleteById(token);
    }
    
}
