package com.renegade.videoondemand.domain.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Genre {
    @Id
    private Integer id;
    private String name;
}
