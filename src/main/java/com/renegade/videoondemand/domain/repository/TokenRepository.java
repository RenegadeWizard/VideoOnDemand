package com.renegade.videoondemand.domain.repository;

import com.renegade.videoondemand.domain.entity.Token;
import com.renegade.videoondemand.domain.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TokenRepository extends CrudRepository<Token, String> {
    List<Token> findAllByUserEquals(User user);
}
