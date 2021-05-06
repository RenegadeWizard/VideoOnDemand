package com.renegade.videoondemand.domain.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class Video {
    @Id
    private Integer id;
    private String name;
    private Integer releaseYear;
    private Integer time;
    private String description;
    @ManyToMany
    private List<Actor> actors;
    @ManyToMany
    private List<Genre> genres;
}
