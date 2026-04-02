package com.rampsecure.rampsecure.equipment.dto;

import com.rampsecure.rampsecure.equipment.model.EquipmentCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckInRequest {
    private UUID equipmentId;
    private EquipmentCondition condition;
    private String notes;
}
