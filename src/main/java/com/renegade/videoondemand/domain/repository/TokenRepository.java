package com.renegade.videoondemand.domain.repository;

import com.renegade.videoondemand.domain.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
}
