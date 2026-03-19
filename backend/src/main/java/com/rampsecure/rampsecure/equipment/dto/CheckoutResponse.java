package com.rampsecure.rampsecure.equipment.dto;

import com.rampsecure.rampsecure.equipment.model.EquipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponse {
    private String transactionId;
    private String equipmentCode;
    private LocalDateTime checkoutAt;
    private EquipmentStatus status;
}
