package com.v1.sealert.sa.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class UserDistrict {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID guid;

    @ManyToOne
    @JoinColumn(name = "user_guid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "district_guid")
    private District district;

    public UserDistrict(User user, District district) {
        this.user = user;
        this.district = district;
    }

    @Override
    public String toString() {
        return "UserDistrict{" +
                "guid=" + guid +
                ", user=" + user +
                ", district=" + district +
                '}';
    }
}
