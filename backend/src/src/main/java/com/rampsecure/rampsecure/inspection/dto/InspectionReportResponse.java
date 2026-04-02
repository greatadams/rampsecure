package com.rampsecure.rampsecure.inspection.dto;


import com.rampsecure.rampsecure.inspection.model.ChecklistStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InspectionReportResponse {
    private UUID reportId;
    private ChecklistStatus checklistStatus;
    private String equipmentCode;
    private LocalDateTime inspectedAt;
    private LocalDateTime expireAt;
    private boolean hasCriticalFailure; //tell frontend whether to block checkout

}
