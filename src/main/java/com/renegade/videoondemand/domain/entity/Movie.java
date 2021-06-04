package com.renegade.videoondemand.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Movie extends Video {
    private Integer time;
    private String description;
}
