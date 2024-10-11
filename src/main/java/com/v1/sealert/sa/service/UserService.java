package com.v1.sealert.sa.service;

import com.v1.sealert.sa.DAO.UserDao;
import com.v1.sealert.sa.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
    public boolean addUser(String name, String chatId) {
        User u = new User(name, chatId);
        return userDao.addUser(u);
    }
    public boolean deleteUser(String name) {
        return userDao.deleteUser(findByName(name).get().getId());
    }
    public Optional<User> findByName(String name) {
        Optional<User> r = userDao.findByName(name);
        return r;
    }
}
