package com.v1.sealert.sa.repo;

import com.v1.sealert.sa.model.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DistrictRepository extends JpaRepository<District, UUID> {
    public Optional<District> findByName(String name);
}
