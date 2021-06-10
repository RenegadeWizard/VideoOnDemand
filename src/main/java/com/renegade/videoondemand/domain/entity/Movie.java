package com.renegade.videoondemand.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@Getter
@Setter
public class Movie extends Video {
    private Integer time;
    private String description;

    @Transient
    public Movie cloneAll(Movie movie) {
        this.name = movie.name;
        this.releaseYear = movie.releaseYear;
        this.time = movie.time;
        this.description = movie.description;
        return this;
    }

    @Transient
    public Movie cloneSome(Movie movie) {
        this.name = movie.name != null ? movie.name : this.name;
        this.releaseYear = movie.releaseYear != null ? movie.releaseYear : this.releaseYear;
        this.time = movie.time != null ? movie.time : this.time;
        this.description = movie.description != null ? movie.description : this.description;
        return this;
    }
}
