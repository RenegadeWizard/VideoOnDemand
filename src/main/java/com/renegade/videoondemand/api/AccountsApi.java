package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.domain.repository.UserRepository;
import com.renegade.videoondemand.exception.ObjectNotInDatabaseException;
import com.renegade.videoondemand.exception.TokenDoesNotExistException;
import com.renegade.videoondemand.exception.UserAlreadyExistsException;
import com.renegade.videoondemand.exception.UserDoesNotExistException;
import com.renegade.videoondemand.service.TokenService;
import com.renegade.videoondemand.util.EtagHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsApi {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @PostMapping
    public void createNewAccount(@RequestParam("token") String token, User user) {
        if (!tokenRepository.existsById(token)) {
            throw new TokenDoesNotExistException();
        }
        if (userRepository.findById(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User user = userRepository
                .findById(username)
                .orElseThrow(UserDoesNotExistException::new);
        return ResponseEntity.ok()
                .eTag(user.getVersion().toString())
                .body(user);
    }

    @PutMapping("/{username}")
    public void updateUser(@PathVariable String username, @RequestBody User user,
                           @RequestHeader(value = "If-Match", required=false) String ifMatch) {
        User userInDatabase = userRepository.findById(username).orElseThrow(UserDoesNotExistException::new);
        EtagHelper.checkEtagCorrectness(userInDatabase.getVersion(), ifMatch);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(userInDatabase.cloneAll(user));
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        User user = userRepository.findById(username).orElseThrow(ObjectNotInDatabaseException::new);
        tokenService.deleteAllUserTokens(user.getUsername());
        userRepository.deleteById(username);
    }

    @PatchMapping("/{username}")
    public void patchUser(@PathVariable String username, @RequestBody User user,
                          @RequestHeader(value = "If-Match", required=false) String ifMatch) {
        User userToUpdate = userRepository.findById(username).orElseThrow(UserDoesNotExistException::new);
        user.setPassword(user.getPassword() != null ? encoder.encode(user.getPassword()) : null);
        EtagHelper.checkEtagCorrectness(userToUpdate.getVersion(), ifMatch);
        userRepository.save(userToUpdate.cloneSome(user));
    }
}
