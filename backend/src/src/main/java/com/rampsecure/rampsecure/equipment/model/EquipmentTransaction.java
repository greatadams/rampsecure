package com.rampsecure.rampsecure.equipment.model;

import com.rampsecure.rampsecure.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

//Who used it? (operator)
//When did they take it? (checkoutAt)
//When did they return it? (checkinAt)
//How long did they use it? (durationMinutes)
//What condition was it in? (condition, conditionNotes)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User operator;

    private LocalDateTime checkoutAt;

    private LocalDateTime checkinAt;

    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    private EquipmentCondition condition;

    private String conditionNotes;
}
