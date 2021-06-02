package com.renegade.videoondemand.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToMany
    private List<Movie> movie;
    @ManyToOne
    private User user;
}
