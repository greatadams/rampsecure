package com.rampsecure.rampsecure.inspection.repository;

import com.rampsecure.rampsecure.inspection.model.InspectionReport;
import com.rampsecure.rampsecure.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface InspectionReportRepository extends JpaRepository<InspectionReport, UUID> {
  Optional<InspectionReport> findByOperatorAndExpiresAtAfter(User operator, LocalDateTime now);
}
