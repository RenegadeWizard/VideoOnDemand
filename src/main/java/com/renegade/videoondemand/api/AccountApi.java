package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.domain.repository.UserRepository;
import com.renegade.videoondemand.exception.FailedAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountApi {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @GetMapping
    public User getUser(@RequestHeader("token") String token) {
        return tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
    }

    @DeleteMapping
    public void deleteUser(@RequestHeader("token") String token) {
        String username = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser().getUsername();
        tokenRepository.deleteById(token);
        userRepository.deleteById(username);
    }


}
