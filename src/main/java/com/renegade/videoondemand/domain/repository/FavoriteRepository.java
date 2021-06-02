package com.renegade.videoondemand.domain.repository;

import com.renegade.videoondemand.domain.entity.Favorite;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends PagingAndSortingRepository<Favorite, Integer> {
}
