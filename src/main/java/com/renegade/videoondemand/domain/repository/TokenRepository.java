package com.renegade.videoondemand.domain.repository;

import com.renegade.videoondemand.domain.entity.PostToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<PostToken, String> {
}
