package com.example.todolist.controllers;


import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.todolist.entities.User;
import com.example.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;



    @PostMapping()
    public ResponseEntity created(@RequestBody User userModel) {
        var user = repository.findByUsername(userModel.getUsername());


        if(user!= null){
            System.out.println("Usuario já existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe usuario");
        }
        var passwordHash = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHash);
        var userCreated = this.repository.save(userModel);
        return  ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
