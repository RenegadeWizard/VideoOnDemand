package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Favorite;
import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.entity.Video;
import com.renegade.videoondemand.domain.repository.FavoritesRepository;
import com.renegade.videoondemand.domain.repository.TokenRepository;
import com.renegade.videoondemand.domain.repository.UserRepository;
import com.renegade.videoondemand.exception.FailedAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoritesApi {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final FavoritesRepository favoritesRepository;

    @GetMapping
    public List<Favorite> getAllFavorites(@RequestHeader("token") String token) {
        if (token == null) {
            throw new FailedAuthenticationException();
        }
        User user = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        return user.getFavorites();
    }

    @PostMapping
    public void addVideoToFavorites(@RequestHeader("token") String token, @RequestBody Video video) {
        User user = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        user.getFavorites().add(new Favorite(null, video, null));
        userRepository.save(user);
    }

    @DeleteMapping("/{fid}")
    public void deleteVideoFromFavorites(@RequestHeader("token") String token, @PathVariable String fid) {
        User user = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        Favorite favorite = favoritesRepository.findById(Integer.parseInt(fid)).orElseThrow(RuntimeException::new);
        user.getFavorites().remove(favorite);
        favoritesRepository.deleteById(Integer.parseInt(fid));
    }

    @PatchMapping("/{fid}")
    public void patchFavorite(@RequestHeader("token") String token, @PathVariable String fid, @RequestBody Favorite rateFavorite) {
        User user = tokenRepository.findById(token).orElseThrow(FailedAuthenticationException::new).getUser();
        Favorite favorite = favoritesRepository.findById(Integer.parseInt(fid)).orElseThrow(RuntimeException::new);
        if (!user.getFavorites().contains(favorite)) {
            throw new FailedAuthenticationException();
        }
        favorite.setRate(rateFavorite.getRate());
        favoritesRepository.save(favorite);
    }

}
