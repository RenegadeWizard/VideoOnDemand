package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Favorite;
import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.entity.Video;
import com.renegade.videoondemand.domain.repository.FavoritesRepository;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.domain.repository.VideoRepository;
import com.renegade.videoondemand.exception.FailedAuthenticationException;
import com.renegade.videoondemand.exception.ObjectNotInDatabaseException;
import com.renegade.videoondemand.util.EtagHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoritesApi {
    private final TokenRepository tokenRepository;
    private final FavoritesRepository favoritesRepository;
    private final VideoRepository videoRepository;

    @GetMapping
    public Page<Favorite> getAllFavorites(@RequestHeader("token") String token, Pageable pageable) {
        if (token == null) {
            throw new FailedAuthenticationException();
        }
        User user = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        return favoritesRepository.findAllByUserEquals(user, pageable);
    }

    @PostMapping
    public void addVideoToFavorites(@RequestHeader("token") String token, @RequestBody Video video) {
        User user = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        Video videoInDatabase = videoRepository.findById(video.getId()).orElseThrow(ObjectNotInDatabaseException::new);
        favoritesRepository.save(new Favorite(videoInDatabase, user));
    }

    @GetMapping("/{fid}")
    public ResponseEntity<Favorite> getFavorite(@RequestHeader("token") String token, @PathVariable String fid) {
        User user = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        Favorite favorite = favoritesRepository.findById(Integer.parseInt(fid)).orElseThrow(ObjectNotInDatabaseException::new);
        if (!favorite.getUser().getUsername().equals(user.getUsername())) {
            throw new FailedAuthenticationException();
        }
        return ResponseEntity.ok()
                .eTag(favorite.getVersion().toString())
                .body(favorite);
    }

    @DeleteMapping("/{fid}")
    public void deleteVideoFromFavorites(@RequestHeader("token") String token, @PathVariable String fid) {
        User user = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        Favorite favorite = favoritesRepository.findById(Integer.parseInt(fid)).orElseThrow(ObjectNotInDatabaseException::new);
        if (!favorite.getUser().getUsername().equals(user.getUsername())) {
            throw new FailedAuthenticationException();
        }
        favoritesRepository.deleteById(Integer.parseInt(fid));
    }

    @PatchMapping("/{fid}")
    public void patchFavorite(@RequestHeader("token") String token,
                              @RequestHeader(value = "If-Match", required=false) String ifMatch,
                              @PathVariable String fid, @RequestBody Favorite rateFavorite) {
        User user = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        Favorite favorite = favoritesRepository.findById(Integer.parseInt(fid)).orElseThrow(ObjectNotInDatabaseException::new);
        if (!favorite.getUser().getUsername().equals(user.getUsername())) {
            throw new FailedAuthenticationException();
        }
        EtagHelper.checkEtagCorrectness(favorite.getVersion(), ifMatch);
        favorite.setRate(rateFavorite.getRate());
        favoritesRepository.save(favorite);
    }

}
