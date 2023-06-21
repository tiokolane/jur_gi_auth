package com.tiokolane.jur_gi_auth.controller;

import com.tiokolane.jur_gi_auth.model.User;
import com.tiokolane.jur_gi_auth.repository.UserRepository;

import lombok.val;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

   
    @Autowired
    private UserRepository userRepository;

   
    @GetMapping(value = "/all")
    public ResponseEntity<List<User>> getAll() {

        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable long id) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/infos")
    public ResponseEntity<User> getByToken() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails jwt = (UserDetails) authentication.getPrincipal();
         Optional<User> user = userRepository.findByUsernameOrEmail(jwt.getUsername(), jwt.getUsername());
        return new ResponseEntity<>(user.get(),HttpStatus.OK);
        

    }

}