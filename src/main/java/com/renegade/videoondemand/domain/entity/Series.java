package com.renegade.videoondemand.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@Getter
@Setter
public class Series extends Video {
    private Integer seasons;

    @Transient
    public Series cloneAll(Series show) {
        super.cloneAll(show);
        this.seasons = show.seasons;
        return this;
    }

    @Transient
    public Series cloneSome(Series show) {
        super.cloneSome(show);
        this.seasons = show.seasons != null ? show.seasons : this.seasons;
        return this;
    }
}
