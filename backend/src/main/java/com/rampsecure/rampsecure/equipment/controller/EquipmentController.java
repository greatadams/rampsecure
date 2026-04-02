package com.rampsecure.rampsecure.equipment.controller;

import com.rampsecure.rampsecure.equipment.model.Equipment;
import com.rampsecure.rampsecure.equipment.model.EquipmentStatus;
import com.rampsecure.rampsecure.equipment.service.EquipmentService;
import com.rampsecure.rampsecure.user.model.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/equipment")
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;

    @GetMapping("/allEquipmentByStation")
    public List<Equipment> getAllEquipmentByStation(@RequestParam Station station) {
        return equipmentService.getAllEquipmentByStation(station);
    }

    @GetMapping("/allAvailableEquipmentByStation")
    public List<Equipment> getAllAvailableEquipmentByStation(@RequestParam Station station) {
        return equipmentService.getAllAvailableEquipmentByStation(station);
    }

    @GetMapping("/getEquipmentByCode")
    public Equipment getEquipmentByCode(@RequestParam  String code) {
        return equipmentService.getEquipmentByCode(code);
    }

@PutMapping("/{id}/status")
    public Equipment updateEquipmentStatus(@PathVariable UUID id, @RequestParam EquipmentStatus status) {
        return equipmentService.updateEquipmentStatus(id,status);
}

    @GetMapping("/all")
    public List<Equipment> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    @PutMapping("/{id}/sendToMaintenance")
    public Equipment sendToMaintenance(@PathVariable UUID id) {
        return equipmentService.updateEquipmentStatus(id, EquipmentStatus.MAINTENANCE);
    }
}
