package com.kannan.ai.service;

import com.kannan.ai.model.Actor;
import com.kannan.ai.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorService {

    @Autowired
    ActorRepository actRepo;
    public Actor save(Actor actor){
     return   actRepo.save(actor);
    }

    public Actor getActor(int id) {
       return actRepo.findById((long) id).get();
    }

    public List<Actor> getAllAccounts(){
        return  actRepo.findAll();
    }
   public Actor updateActor(Actor act){
       return actRepo.save(act);
   }

   public boolean deleteById(int id){
        actRepo.deleteById((long)id);
        return true;
   }
}
