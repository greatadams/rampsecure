package com.rampsecure.rampsecure.inspection.dto;

import com.rampsecure.rampsecure.inspection.model.ResultStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResultRequest {
    private UUID checkListItemId;
    private ResultStatus resultStatus;
    private String notes;
}
