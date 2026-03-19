package com.rampsecure.rampsecure.equipment.service;

import com.rampsecure.rampsecure.equipment.model.Equipment;
import com.rampsecure.rampsecure.equipment.model.EquipmentStatus;
import com.rampsecure.rampsecure.equipment.repository.EquipmentRepository;
import com.rampsecure.rampsecure.user.model.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    public List<Equipment> getAllEquipmentByStation(Station station) {
      return equipmentRepository.findByStation(station);
    }

    public List<Equipment> getAllAvailableEquipmentByStation(Station station) {
        EquipmentStatus status = EquipmentStatus.AVAILABLE;
      return equipmentRepository.findByStationAndStatus(station, status);

    }

    public Equipment getEquipmentByCode(String code) {
        return equipmentRepository.findByEquipmentCode(code).orElse(null);
    }
}
