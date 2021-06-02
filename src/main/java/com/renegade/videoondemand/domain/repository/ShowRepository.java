package com.renegade.videoondemand.domain.repository;

import com.renegade.videoondemand.domain.entity.Series;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends PagingAndSortingRepository<Series, Integer> {
}
