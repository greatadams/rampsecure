package com.rampsecure.rampsecure.equipment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rampsecure.rampsecure.inspection.model.EquipmentType;
import com.rampsecure.rampsecure.user.model.Station;
import com.rampsecure.rampsecure.user.model.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_operator_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User currentOperator;

    @Column(nullable = false,unique = true)
    private String equipmentCode;

    @Enumerated(EnumType.STRING)
    private EquipmentType equipmentType;

    private String model;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;

    @Enumerated(EnumType.STRING)
    private Station station;

    private String location;

    private LocalDateTime lastCheckoutAt;


    public UUID getCurrentOperatorId() {
        return currentOperator != null ? currentOperator.getId() : null;
    }
}
