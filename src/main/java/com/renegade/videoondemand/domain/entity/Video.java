package com.renegade.videoondemand.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;
    protected String name;
    protected Integer releaseYear;
    @JsonIgnore
    @Version
    private Integer version;

    @Transient
    public void cloneAll(Video video) {
        this.name = video.name;
        this.releaseYear = video.releaseYear;
    }

    @Transient
    public void cloneSome(Video video) {
        this.name = video.name != null ? video.name : this.name;
        this.releaseYear = video.releaseYear != null ? video.releaseYear : this.releaseYear;
    }
}
