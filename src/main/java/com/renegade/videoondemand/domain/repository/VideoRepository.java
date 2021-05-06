package com.renegade.videoondemand.domain.repository;

import com.renegade.videoondemand.domain.entity.Video;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends CrudRepository<Video, Integer> {
}
