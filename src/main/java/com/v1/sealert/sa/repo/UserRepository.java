package com.v1.sealert.sa.repo;

import com.v1.sealert.sa.DTO.UserDistrictDTO;
import com.v1.sealert.sa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    public Optional<User> findByName(String name);

    @Query("Select distinct u from User u Join fetch u.userDistrict")
    public List<User> findAllWithDistricts();
}
