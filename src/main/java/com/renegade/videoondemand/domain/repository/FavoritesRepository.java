package com.renegade.videoondemand.domain.repository;

import com.renegade.videoondemand.domain.entity.Favorite;
import com.renegade.videoondemand.domain.entity.User;
import com.renegade.videoondemand.domain.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritesRepository extends CrudRepository<Favorite, Integer> {
    List<Favorite> findAllByUserEquals(User user);
    Page<Favorite> findAllByUserEquals(User user, Pageable pageable);
    List<Favorite> findAllByVideoEquals(Video video);
}
