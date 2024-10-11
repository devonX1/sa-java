package com.v1.sealert.sa.repo;

import com.v1.sealert.sa.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJdbcTemplateRepository {
    public List<User> getAllUsers();
    public boolean addUser(User u);
    public boolean deleteUser(UUID id);
    public Optional<User> findByName(String name);
}
