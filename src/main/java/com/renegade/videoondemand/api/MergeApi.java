package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Token;
import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.domain.repository.UserRepository;
import com.renegade.videoondemand.exception.ObjectNotInDatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merge")
@RequiredArgsConstructor
public class MergeApi {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @PostMapping("/{username1}/{username2}")
    public void mergeTwoAccounts(@PathVariable String username1, @PathVariable String username2) {
        User user1 = userRepository.findById(username1).orElseThrow(ObjectNotInDatabaseException::new);
        User user2 = userRepository.findById(username2).orElseThrow(ObjectNotInDatabaseException::new);
        user1.getFavorites().addAll(user2.getFavorites());
        for (Token userToken: user2.getTokens()) {
            tokenRepository.deleteById(userToken.getValue());
        }
        userRepository.deleteById(user2.getUsername());
    }
}
