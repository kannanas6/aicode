package com.kannan.ai.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "actor")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "movie_fk_id")
    private Movie movie;

    @OneToMany(mappedBy = "actor")
    private List<ActorHomeId> listActorHomeIds;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public List<ActorHomeId> getListActorHomeIds() {
        return listActorHomeIds;
    }

    public void setListActorHomeIds(List<ActorHomeId> listActorHomeIds) {
        this.listActorHomeIds = listActorHomeIds;
    }
}