package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Token;
import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.domain.repository.UserRepository;
import com.renegade.videoondemand.exception.FailedAuthenticationException;
import com.renegade.videoondemand.exception.ObjectNotInDatabaseException;
import com.renegade.videoondemand.exception.UserAlreadyExistsException;
import com.renegade.videoondemand.exception.UserDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsApi {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping
    public void createNewAccount(User user) {
        if (userRepository.findById(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userRepository.findById(username).orElseThrow(UserDoesNotExistException::new);
    }

    @PutMapping("/{username}")
    public void updateUser(@PathVariable String username, @RequestBody User user) {
        user.setUsername(username);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        User user = userRepository.findById(username).orElseThrow(ObjectNotInDatabaseException::new);
        for (Token userToken: user.getTokens()) {
            tokenRepository.deleteById(userToken.getValue());
        }
        userRepository.deleteById(username);
    }

    @PatchMapping("/{username}")
    public void patchUser(@PathVariable String username, @RequestBody User user) {
        User userToUpdate = userRepository.findById(username).orElseThrow(FailedAuthenticationException::new);
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            userToUpdate.setPassword(encoder.encode(user.getPassword()));
        }
    }
}
