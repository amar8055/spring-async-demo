package com.techie.async.rest.api.controller;

import com.techie.async.rest.api.entity.User;
import com.techie.async.rest.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/saveUsers", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseEntity saveUser(@RequestParam(name = "files") MultipartFile[] files) throws Exception {
        for(MultipartFile file: files) {
            userService.saveUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/getAllUsers", produces = "application/json")
    public CompletableFuture<ResponseEntity<List<User>>> getAllUsers() {
        return userService.getAllUsers().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/getUsers", produces = "application/json")
    public ResponseEntity<CompletableFuture<List<User>>> getUsers() {
        CompletableFuture<List<User>> user1 = userService.getAllUsers();
        CompletableFuture<List<User>> user2 = userService.getAllUsers();
        CompletableFuture<List<User>> user3 = userService.getAllUsers();
        CompletableFuture.allOf(user1,user2,user3).join();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
