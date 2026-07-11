package com.kannan.ai.controller;
import com.kannan.ai.model.Actor;
import com.kannan.ai.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ActorandMovieController {

    @Autowired
    ActorService actorService;
    @PostMapping("/saveActor")
    public ResponseEntity<Actor> saveActor(@RequestBody Actor actor){
       actor= actorService.save(actor);
        return new ResponseEntity<>(actor, HttpStatus.CREATED);

    }



    @GetMapping("/getActor/{id}/{count}")
    public ResponseEntity<Actor> getActor(@PathVariable int id, @PathVariable int count){
System.out.println(count);
         return new ResponseEntity<>(actorService.getActor(id), HttpStatus.OK);
    }

    @PutMapping("/updateActor")
    public ResponseEntity<Actor> updateActor(@RequestBody Actor act){
      return  new ResponseEntity<>(actorService.updateActor(act), HttpStatus.OK);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteByid(@PathVariable int id){
       boolean chk= actorService.deleteById(id);
        String res="";
       if(chk) {
            res = "Successfully Deleted Id" + id;
       }
       else {
           res="Not available id "+id;
       }
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @GetMapping("/getAllActors")
    public ResponseEntity<List<Actor>> getActors(){

     return   new ResponseEntity<>(actorService.getAllAccounts(), HttpStatus.NOT_FOUND);
    }
}
