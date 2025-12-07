package com.purrfect.pet_service.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pets")
@Data
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String breed;
    private double age;
    private String gender;
    private String health;
    private String personality;
    private String photoPath;
    private String status;
}






