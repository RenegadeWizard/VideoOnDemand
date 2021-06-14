package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Favorite;
import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.entity.Video;
import com.renegade.videoondemand.domain.repository.FavoritesRepository;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.domain.repository.VideoRepository;
import com.renegade.videoondemand.exception.FailedAuthenticationException;
import com.renegade.videoondemand.exception.ObjectNotInDatabaseException;
import com.renegade.videoondemand.exception.TokenDoesNotExistException;
import com.renegade.videoondemand.service.SessionService;
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
    private final SessionService sessionService;
    private final FavoritesRepository favoritesRepository;
    private final VideoRepository videoRepository;
    private final TokenRepository tokenRepository;

    @GetMapping
    public Page<Favorite> getAllFavorites(@RequestHeader("sessionID") String sessionID, Pageable pageable) {
        if (sessionID == null) {
            throw new FailedAuthenticationException();
        }
        User user = sessionService.getUserByToken(sessionID);
        return favoritesRepository.findAllByUserEquals(user, pageable);
    }

    @PostMapping
    public void addVideoToFavorites(@RequestHeader("sessionID") String session,
                                    @RequestParam("token") String token,
                                    @RequestBody Video video) {
        User user = sessionService.getUserByToken(session);
        if (!tokenRepository.existsById(token)) {
            throw new TokenDoesNotExistException();
        }
        tokenRepository.deleteById(token);
        Video videoInDatabase = videoRepository.findById(video.getId()).orElseThrow(ObjectNotInDatabaseException::new);
        favoritesRepository.save(new Favorite(videoInDatabase, user));
    }

    @GetMapping("/{fid}")
    public ResponseEntity<Favorite> getFavorite(@RequestHeader("sessionID") String sessionID,
                                                @PathVariable String fid) {
        User user = sessionService.getUserByToken(sessionID);
        Favorite favorite = favoritesRepository.findById(Integer.parseInt(fid)).orElseThrow(ObjectNotInDatabaseException::new);
        if (!favorite.getUser().getUsername().equals(user.getUsername())) {
            throw new FailedAuthenticationException();
        }
        return ResponseEntity.ok()
                .eTag(favorite.getVersion().toString())
                .body(favorite);
    }

    @DeleteMapping("/{fid}")
    public void deleteVideoFromFavorites(@RequestHeader("sessionID") String sessionID,
                                         @PathVariable String fid) {
        User user = sessionService.getUserByToken(sessionID);
        Favorite favorite = favoritesRepository.findById(Integer.parseInt(fid)).orElseThrow(ObjectNotInDatabaseException::new);
        if (!favorite.getUser().getUsername().equals(user.getUsername())) {
            throw new FailedAuthenticationException();
        }
        favoritesRepository.deleteById(Integer.parseInt(fid));
    }

    @PatchMapping("/{fid}")
    public void patchFavorite(@RequestHeader("sessionID") String sessionID,
                              @RequestHeader(value = "If-Match", required=false) String ifMatch,
                              @PathVariable String fid, @RequestBody Favorite rateFavorite) {
        User user = sessionService.getUserByToken(sessionID);
        Favorite favorite = favoritesRepository.findById(Integer.parseInt(fid)).orElseThrow(ObjectNotInDatabaseException::new);
        if (!favorite.getUser().getUsername().equals(user.getUsername())) {
            throw new FailedAuthenticationException();
        }
        EtagHelper.checkEtagCorrectness(favorite.getVersion(), ifMatch);
        favorite.setRate(rateFavorite.getRate());
        favoritesRepository.save(favorite);
    }

}
