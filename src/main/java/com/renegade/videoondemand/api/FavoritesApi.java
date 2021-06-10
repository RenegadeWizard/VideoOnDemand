package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Favorite;
import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.entity.Video;
import com.renegade.videoondemand.domain.repository.FavoritesRepository;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.exception.FailedAuthenticationException;
import com.renegade.videoondemand.exception.ObjectNotInDatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoritesApi {
    private final TokenRepository tokenRepository;
    private final FavoritesRepository favoritesRepository;

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
        favoritesRepository.save(new Favorite(null, video, user, null));
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
    public void patchFavorite(@RequestHeader("token") String token, @PathVariable String fid, @RequestBody Favorite rateFavorite) {
        User user = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        Favorite favorite = favoritesRepository.findById(Integer.parseInt(fid)).orElseThrow(ObjectNotInDatabaseException::new);
        if (!favorite.getUser().getUsername().equals(user.getUsername())) {
            throw new FailedAuthenticationException();
        }
        favorite.setRate(rateFavorite.getRate());
        favoritesRepository.save(favorite);
    }

}
