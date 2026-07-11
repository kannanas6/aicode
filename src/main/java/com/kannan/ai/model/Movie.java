package com.kannan.ai.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="movie")
public class Movie {



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column
    int movieId;

    @Column
    String movieName;


    @OneToMany(mappedBy="movie", cascade = CascadeType.ALL)
    private List<Actor> actors=new ArrayList<>();

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }



}
