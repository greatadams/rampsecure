package com.rampsecure.rampsecure.inspection.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

//individual check item
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String itemName;
    private String description;
    private boolean isCritical;
    @Enumerated(EnumType.STRING)
    private EquipmentType template;
}
