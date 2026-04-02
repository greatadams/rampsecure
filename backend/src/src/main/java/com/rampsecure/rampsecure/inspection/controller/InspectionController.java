package com.rampsecure.rampsecure.inspection.controller;

import com.rampsecure.rampsecure.inspection.dto.InspectionReportResponse;
import com.rampsecure.rampsecure.inspection.dto.StartInspectionRequest;
import com.rampsecure.rampsecure.inspection.dto.SubmitInspectionRequest;
import com.rampsecure.rampsecure.inspection.model.ChecklistItem;
import com.rampsecure.rampsecure.inspection.service.InspectionService;
import com.rampsecure.rampsecure.security.AuthDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inspection")
@RequiredArgsConstructor
public class InspectionController {

    private final InspectionService inspectionService;

    @PostMapping("/start")
    public InspectionReportResponse startInspection(
            @Valid
            @RequestBody
            StartInspectionRequest startInspectionRequest) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        AuthDetails details = (AuthDetails) auth.getDetails();
        UUID operatorId = details.getUserId();
        return inspectionService.inspectionReport(operatorId, startInspectionRequest.getEquipmentId());
    }

    @PostMapping("/submit")
    public InspectionReportResponse submitInspection(
            @Valid
            @RequestBody
            SubmitInspectionRequest submitInspectionRequest){
      return inspectionService.submitInspectionReport(submitInspectionRequest.getReportId(),submitInspectionRequest.getResults());
    }

@GetMapping("/checklist/{equipmentId}")
public List<ChecklistItem> getChecklist(@PathVariable UUID equipmentId) {
    return inspectionService.getChecklistByEquipment(equipmentId);
}
}


