package com.app.ecom.Controller;

import com.app.ecom.Dto.userRequest;
import com.app.ecom.Dto.userResponse;
import com.app.ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class UserController {

@Autowired
private final UserService us;

    public UserController(UserService us) {
        this.us = us;
    }


    @GetMapping("/api/users")
    public ResponseEntity<List<userResponse>> getAllUser() {
        return new ResponseEntity<>(us.fetchAllUser(),HttpStatus.OK);
    }

    @PostMapping ("/api/users")
    public ResponseEntity<userRequest> createUser(@RequestBody userRequest userReq) {
        userRequest added= us.addUser(userReq);
        return new ResponseEntity<>(added,HttpStatus.CREATED);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<userResponse> getSpecificUser(@PathVariable Long id){
        return us.getUserById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(()-> ResponseEntity.notFound().build());


    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,@RequestBody userRequest Updated ){
       boolean updated = us.updateUserById(id,Updated);

        if(updated)
            return ResponseEntity.ok("user updated successfully");
        return  ResponseEntity.notFound().build();
    }



}
