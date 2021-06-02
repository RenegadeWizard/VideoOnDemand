package com.renegade.videoondemand.domain.repository;

import com.renegade.videoondemand.domain.entity.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {
}
