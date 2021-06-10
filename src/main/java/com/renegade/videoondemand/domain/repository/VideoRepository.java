package com.renegade.videoondemand.domain.repository;

import com.renegade.videoondemand.domain.entity.Video;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VideoRepository extends PagingAndSortingRepository<Video, Integer> {
}
