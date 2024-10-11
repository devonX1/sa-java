package com.v1.sealert.sa.service;

import com.v1.sealert.sa.model.User;
import com.v1.sealert.sa.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
    public User addUser(String name, String chatId) {
        User u = new User(name, chatId);
        return userRepository.save(u);
    }
    public void deleteUser(String name) {
        userRepository.deleteById(userRepository.findByName(name).get().getGuid());
    }
    public Optional<User> findByName(String name) {
        Optional<User> r = userRepository.findByName(name);
        return r;
    }
}
