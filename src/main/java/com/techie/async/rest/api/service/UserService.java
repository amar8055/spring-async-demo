package com.techie.async.rest.api.service;

import com.techie.async.rest.api.entity.User;
import com.techie.async.rest.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Async
    public CompletableFuture<List<User>> saveUsers(final MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        List<User> users = parseCsv(file);
        logger.info("Saving list of users of size {}",users.size(),""+Thread.currentThread().getName());
        users=userRepository.saveAll(users);
        long end = System.currentTimeMillis();
        logger.info("Total time taken to process " +(end-start));
        return CompletableFuture.completedFuture(users);
    }

    @Async
    public CompletableFuture<List<User>> getAllUsers() {
        logger.info("get list of user by " +Thread.currentThread().getName());
        List<User> users = userRepository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    private List<User> parseCsv(MultipartFile file) throws Exception {
        final List<User> userList = new ArrayList<>();
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String lines;
            while ((lines = br.readLine()) != null) {
                String[] data = lines.split(",");
                User user = new User();
                user.setName(data[0]);
                user.setEmail(data[1]);
                user.setGender(data[2]);
                userList.add(user);
            }
            return userList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
