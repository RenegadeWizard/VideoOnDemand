package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.domain.repository.UserRepository;
import com.renegade.videoondemand.exception.FailedAuthenticationException;
import com.renegade.videoondemand.util.EtagHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<User> getUser(@RequestHeader("token") String token) {
        User user = tokenRepository
                .findById(token)
                .orElseThrow(FailedAuthenticationException::new)
                .getUser();
        return ResponseEntity.ok()
                .eTag(user.getVersion().toString())
                .body(user);
    }

    @PutMapping
    public void updateUser(@RequestHeader("token") String token, @RequestBody User user,
                           @RequestHeader(value = "If-Match", required=false) String ifMatch) {
        User userInDatabase = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        EtagHelper.checkEtagCorrectness(userInDatabase.getVersion(), ifMatch);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(userInDatabase.cloneAll(user));
    }

    @DeleteMapping
    public void deleteUser(@RequestHeader("token") String token) {
        User user = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        tokenRepository.findAllByUserEquals(user).forEach(tokenRepository::delete);
        userRepository.deleteById(user.getUsername());
    }

    @PatchMapping
    public void patchUser(@RequestHeader("token") String token, @RequestBody User user,
                          @RequestHeader(value = "If-Match", required=false) String ifMatch) {
        User userInDatabase = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        EtagHelper.checkEtagCorrectness(userInDatabase.getVersion(), ifMatch);
        user.setPassword(user.getPassword() != null ? encoder.encode(user.getPassword()) : null);
        userRepository.save(userInDatabase.cloneSome(user));
    }


}
