package com.v1.sealert.sa.repo;

import com.v1.sealert.sa.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User, UUID>{
    public Optional<User> findByName(String name);
}
