package com.rampsecure.rampsecure.equipment.dto;


import com.rampsecure.rampsecure.equipment.model.EquipmentCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckinResponse {
    private String transactionId;
    private String equipmentCode;
    private LocalDateTime checkinAt;
    private Integer durationMinutes;
    private EquipmentCondition condition;



}
