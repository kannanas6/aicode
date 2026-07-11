package com.kannan.ai.model;

import jakarta.persistence.*;

@Entity
@Table(name = "actor_home_id")
public class ActorHomeId {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @ManyToOne
    @JoinColumn(name = "actor_fk_id")
    private Actor actor;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}