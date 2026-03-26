package com.rampsecure.rampsecure.inspection.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubmitInspectionRequest {
    private UUID reportId;
    List<ItemResultRequest> results;
}
