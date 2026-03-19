package com.rampsecure.rampsecure.equipment.repository;

import com.rampsecure.rampsecure.equipment.model.Equipment;
import com.rampsecure.rampsecure.equipment.model.EquipmentStatus;
import com.rampsecure.rampsecure.user.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EquipmentRepository extends JpaRepository<Equipment, UUID> {
    List<Equipment> findByStation(Station station);
    List<Equipment> findByStationAndStatus(Station station, EquipmentStatus status);
    Optional<Equipment> findByEquipmentCode(String equipmentCode);
}
