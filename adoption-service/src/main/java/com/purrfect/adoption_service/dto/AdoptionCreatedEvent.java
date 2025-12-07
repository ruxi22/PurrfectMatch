package com.purrfect.adoption_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdoptionCreatedEvent {
    private Long adoptionId;
    private Long userId;
    private Long petId;
    private String petName;
    private String userName;
    private LocalDateTime appointmentDateTime;
}






