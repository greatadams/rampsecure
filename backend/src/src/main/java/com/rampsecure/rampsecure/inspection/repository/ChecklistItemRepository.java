package com.rampsecure.rampsecure.inspection.repository;

import com.rampsecure.rampsecure.inspection.model.ChecklistItem;
import com.rampsecure.rampsecure.inspection.model.EquipmentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, UUID> {
    List<ChecklistItem> findByTemplate(EquipmentType template);

}
