package com.v1.sealert.sa.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID guid;
    private String name;
    private LocalDateTime dateCreate;
    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDistrict> userDistrict = new ArrayList<>();

    public District(String name) {
        this.guid = UUID.randomUUID();
        this.name = name;
        dateCreate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "District{" +
                "dateCreate=" + dateCreate +
                ", name='" + name + '\'' +
                ", guid=" + guid +
                '}';
    }
}
