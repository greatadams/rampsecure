package com.rampsecure.rampsecure.equipment.controller;

import com.rampsecure.rampsecure.equipment.model.Equipment;
import com.rampsecure.rampsecure.equipment.service.EquipmentService;
import com.rampsecure.rampsecure.user.model.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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



}
