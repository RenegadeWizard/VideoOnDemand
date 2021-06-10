package com.renegade.videoondemand.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private Video video;
    @JsonIgnore
    @OneToOne
    private User user;
    private String rate;
    @JsonIgnore
    @Version
    private Integer version;

    public Favorite(Video video, User user) {
        this.video = video;
        this.user = user;
    }

}
