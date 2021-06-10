package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.PostToken;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.exception.ObjectNotInDatabaseException;
import com.renegade.videoondemand.util.EtagHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@RestController
@RequestMapping("/post-tokens")
@RequiredArgsConstructor
public class PostTokenApi {
    private final TokenRepository tokenRepository;

    @PostMapping
    public PostToken createToken() {
        SecureRandom random = new SecureRandom();
        String tokenValue = Base64.getEncoder().encodeToString(random.generateSeed(16));
        PostToken token = new PostToken(tokenValue, false, null, null);
        tokenRepository.save(token);
        return token;
    }

    @GetMapping("/{token}")
    public ResponseEntity<PostToken> getToken(@PathVariable String token) {
        PostToken postToken = tokenRepository.findById(token).orElseThrow(ObjectNotInDatabaseException::new);
        return ResponseEntity.ok()
                .eTag(postToken.getVersion().toString())
                .body(postToken);
    }

    @PatchMapping("/{token}")
    public void changeTokenState(@PathVariable String token,
                                 @RequestHeader(value = "If-Match", required=false) String ifMatch) {
        PostToken postToken = tokenRepository.findById(token).orElseThrow(ObjectNotInDatabaseException::new);
        EtagHelper.checkEtagCorrectness(postToken.getVersion(), ifMatch);
        postToken.setIsUsed(true);
        postToken.setWhenUsed(LocalDateTime.now());
        tokenRepository.save(postToken);
    }
}
