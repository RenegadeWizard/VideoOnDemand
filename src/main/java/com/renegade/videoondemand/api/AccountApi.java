package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.domain.repository.UserRepository;
import com.renegade.videoondemand.exception.FailedAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountApi {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping
    public User getUser(@RequestHeader("token") String token) {
        return tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
    }

    @PutMapping
    public void updateUser(@RequestHeader("token") String token, @RequestBody User user) {
        String username = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser().getUsername();
        user.setUsername(username);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @DeleteMapping
    public void deleteUser(@RequestHeader("token") String token) {
        User user = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        tokenRepository.findAllByUserEquals(user).forEach(tokenRepository::delete);
        userRepository.deleteById(user.getUsername());
    }

    @PatchMapping
    public void patchUser(@RequestHeader("token") String token, @RequestBody User user) {
        User userToUpdate = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            userToUpdate.setPassword(encoder.encode(user.getPassword()));
        }
    }


}
