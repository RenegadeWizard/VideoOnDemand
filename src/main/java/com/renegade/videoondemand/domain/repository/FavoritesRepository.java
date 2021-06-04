package com.renegade.videoondemand.domain.repository;

import com.renegade.videoondemand.domain.entity.Favorite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritesRepository extends CrudRepository<Favorite, Integer> {
}
