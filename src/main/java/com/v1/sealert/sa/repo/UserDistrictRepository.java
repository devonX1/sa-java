package com.v1.sealert.sa.repo;

import com.v1.sealert.sa.model.District;
import com.v1.sealert.sa.model.User;
import com.v1.sealert.sa.model.UserDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserDistrictRepository extends JpaRepository<UserDistrict, UUID> {

    Optional<UserDistrict> findByUserAndDistrict(User u, District d);

    @Modifying
    @Query("DELETE FROM UserDistrict ud WHERE ud.user.guid = :userId AND ud.district.guid = :districtId")
    void deleteUserDistrictByUserAndDistrict(@Param("userId") UUID userId, @Param("districtId") UUID districtId);
}
