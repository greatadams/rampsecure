package com.rampsecure.rampsecure.inspection.service;

import com.rampsecure.rampsecure.equipment.model.Equipment;
import com.rampsecure.rampsecure.equipment.repository.EquipmentRepository;
import com.rampsecure.rampsecure.inspection.dto.InspectionReportResponse;
import com.rampsecure.rampsecure.inspection.dto.ItemResultRequest;
import com.rampsecure.rampsecure.inspection.model.*;
import com.rampsecure.rampsecure.inspection.repository.ChecklistItemRepository;
import com.rampsecure.rampsecure.inspection.repository.InspectionReportRepository;
import com.rampsecure.rampsecure.inspection.repository.ItemResultRepository;
import com.rampsecure.rampsecure.user.model.User;
import com.rampsecure.rampsecure.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InspectionService {
    private final ChecklistItemRepository checklistItemRepository;
    private final InspectionReportRepository inspectionReportRepository;
    private final ItemResultRepository itemResultRepository;
   private final UserRepository userRepository;
   private final EquipmentRepository equipmentRepository;

    public InspectionReportResponse inspectionReport(UUID operatorId,UUID equipmentId) {
    //check if equipment has been inspected already
        //->FETCH USER
        User operator = userRepository.findById(operatorId)
                .orElseThrow(() -> new RuntimeException("Operator not found"));
        //->FETCH EQUIPMENT
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        //now check if inspection already exist
        Optional<InspectionReport> existing = inspectionReportRepository.findByOperatorAndExpiresAtAfter(operator, LocalDateTime.now());

        if (existing.isPresent()) {
            InspectionReport inspectionReport = existing.get();
            return  new InspectionReportResponse(
                    inspectionReport.getId(),
                    inspectionReport.getStatus(),
                    inspectionReport.getEquipment().getEquipmentCode(),
                    inspectionReport.getInspectedAt(),
                    inspectionReport.getExpiresAt(),
                    false
            );
        }

     //if not inspected
        InspectionReport inspectionReport = new InspectionReport();
        inspectionReport.setInspectedAt(LocalDateTime.now());
        inspectionReport.setExpiresAt(LocalDateTime.now().plusHours(8));
        inspectionReport.setStatus(ChecklistStatus.INCOMPLETE);
        inspectionReport.setOperator(operator);
        inspectionReport.setEquipment(equipment);


        //return response
        InspectionReport savedInspectionReport = inspectionReportRepository.save(inspectionReport);
        return new InspectionReportResponse(
                savedInspectionReport.getId(),
                savedInspectionReport.getStatus(),
                savedInspectionReport.getEquipment().getEquipmentCode(),
                savedInspectionReport.getInspectedAt(),
                savedInspectionReport.getExpiresAt(),
                false
        );

    }

   public InspectionReportResponse submitInspectionReport(UUID reportId, List<ItemResultRequest>results) {
        //find report id
    InspectionReport report = inspectionReportRepository.findById(reportId).orElseThrow(() -> new RuntimeException("Inspection Report not found"));
    //check if report has been submitted
       if (report.getStatus() != (ChecklistStatus.INCOMPLETE)) {
           throw new RuntimeException("Inspection Already submitted");
       }

       boolean[] hasCriticalFailure = {false};
       results.forEach(result -> {
           //fetch the checklistItem entity
          ChecklistItem checklistItem = checklistItemRepository.findById(result.getCheckListItemId()).orElseThrow(() -> new RuntimeException("Checklist Item not found"));


           ItemResult itemResult = new ItemResult();
           itemResult.setReport(report);
           itemResult.setItem(checklistItem);
           itemResult.setResult(result.getResultStatus());
           itemResult.setNotes(result.getNotes());
           //check for critical failure

           if (itemResult.getResult()==ResultStatus.FAIL && itemResult.getItem().isCritical()){
               hasCriticalFailure[0] = true;
           }
           itemResultRepository.save(itemResult);

       });
       if (hasCriticalFailure[0]) {
           report.setStatus(ChecklistStatus.FAILED);
       }else {
           report.setStatus(ChecklistStatus.PASSED);
       }
          InspectionReport savedInspectionReport = inspectionReportRepository.save(report);
          return new InspectionReportResponse(
                  savedInspectionReport.getId(),
                  savedInspectionReport.getStatus(),
                  savedInspectionReport.getEquipment().getEquipmentCode(),
                  savedInspectionReport.getInspectedAt(),
                  savedInspectionReport.getExpiresAt(),
                  hasCriticalFailure[0]


          );
   }

   public List<ChecklistItem> getChecklistByEquipment(UUID equipmentId){
        Equipment equipment = equipmentRepository.findById(equipmentId).orElseThrow(() -> new RuntimeException("Equipment not found"));

        //return equipment type and fetch matching checklist item
       return  checklistItemRepository.findByTemplate(equipment.getEquipmentType());
    }
}
