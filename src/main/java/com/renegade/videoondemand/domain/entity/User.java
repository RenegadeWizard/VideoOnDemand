package com.renegade.videoondemand.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class User {
    @Id
    private String username;
    @JsonIgnore
    private String password;
    private String email;
}
