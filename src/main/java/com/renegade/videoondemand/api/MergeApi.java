package com.renegade.videoondemand.api;

import com.renegade.videoondemand.domain.entity.Favorite;
import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.entity.Video;
import com.renegade.videoondemand.domain.repository.FavoritesRepository;
import com.renegade.videoondemand.domain.repository.UserRepository;
import com.renegade.videoondemand.exception.ObjectNotInDatabaseException;
import com.renegade.videoondemand.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/merge")
@RequiredArgsConstructor
public class MergeApi {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final FavoritesRepository favoritesRepository;

    @PostMapping("/{username1}/{username2}")
    public void mergeTwoAccounts(@PathVariable String username1, @PathVariable String username2) {
        User user1 = userRepository.findById(username1).orElseThrow(ObjectNotInDatabaseException::new);
        User user2 = userRepository.findById(username2).orElseThrow(ObjectNotInDatabaseException::new);
        mergeFavorites(user1, user2);
        tokenService.deleteAllUserTokens(user2.getUsername());
        userRepository.deleteById(user2.getUsername());
    }

    private void mergeFavorites(User user1, User user2) {
        List<Integer> userFavorites = favoritesRepository.findAllByUserEquals(user1)
                .stream()
                .map(Favorite::getVideo)
                .map(Video::getId)
                .collect(Collectors.toList());
        for (Favorite favorite : favoritesRepository.findAllByUserEquals(user2)) {
            if (userFavorites.contains(favorite.getVideo().getId())) {
                favoritesRepository.delete(favorite);
            } else {
                favorite.setUser(user1);
                favoritesRepository.save(favorite);
            }
        }
    }
}
