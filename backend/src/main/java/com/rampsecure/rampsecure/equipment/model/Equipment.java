package com.rampsecure.rampsecure.equipment.model;

import com.rampsecure.rampsecure.user.model.Station;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
//What is it? (type, model, equipmentCode)
//Where is it? (station, location)
//What state is it in right now? (status, lastCheckoutAt)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String equipmentCode;

    private String type;

    private String model;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;

    @Enumerated(EnumType.STRING)
    private Station station;

    private String location;

    private LocalDateTime lastCheckoutAt;
}
