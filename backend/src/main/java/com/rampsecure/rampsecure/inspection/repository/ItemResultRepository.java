package com.rampsecure.rampsecure.inspection.repository;

import com.rampsecure.rampsecure.inspection.model.InspectionReport;
import com.rampsecure.rampsecure.inspection.model.ItemResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemResultRepository extends JpaRepository<ItemResult, UUID> {

    List<ItemResult> findByReport(InspectionReport report);
}
