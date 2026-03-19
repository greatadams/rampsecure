package com.rampsecure.rampsecure.equipment.service;

import com.rampsecure.rampsecure.equipment.dto.CheckinResponse;
import com.rampsecure.rampsecure.equipment.dto.CheckoutResponse;
import com.rampsecure.rampsecure.equipment.model.*;
import com.rampsecure.rampsecure.equipment.repository.EquipmentRepository;
import com.rampsecure.rampsecure.equipment.repository.EquipmentTransactionRepository;
import com.rampsecure.rampsecure.user.model.User;
import com.rampsecure.rampsecure.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentTransactionRepository equipmentTransactionRepository;
    private final UserRepository userRepository;

    public CheckoutResponse checkOut(UUID equipmentId, UUID operatorId) {
        //Check if equipment is available
        Equipment equipment = equipmentRepository.findById(equipmentId).orElseThrow(() -> new RuntimeException("Equipment not found"));

        //check operator is valid
        User operator = userRepository.findById(operatorId).orElseThrow(() -> new RuntimeException("Operator not found"));

        //check current status
        if (equipment.getStatus() != EquipmentStatus.AVAILABLE){
            throw new RuntimeException("Equipment not available. Current status is " + equipment.getStatus());
        }

        //check equipment station Match operator station
        if (equipment.getStation() != operator.getStation()){
            throw new RuntimeException("Equipment belongs to different stations");
        }

       // After all the checks pass,

       // Set equipment status to IN_USE
        equipment.setStatus(EquipmentStatus.IN_USE);
        // Set lastCheckoutAt to now
        LocalDateTime now = LocalDateTime.now();
        equipment.setLastCheckoutAt(now);

       // Save the equipment
        Equipment savedEquipment = equipmentRepository.save(equipment);

        //Create a new EquipmentTransaction with operator, equipment, checkoutAt, status CHECKED_OUT
        EquipmentTransaction equipmentTransaction = new EquipmentTransaction();
        equipmentTransaction.setOperator(operator);
        equipmentTransaction.setEquipment(savedEquipment);
        equipmentTransaction.setCheckoutAt(now);
        equipmentTransaction.setCheckoutAt(now);
        equipmentTransaction.setCheckoutAt(now);
        equipmentTransaction.setStatus(TransactionStatus.CHECKED_OUT);


       // Save the transaction

     EquipmentTransaction savedEquipmentTransaction=equipmentTransactionRepository.save(equipmentTransaction);
        //Return CheckoutResponse
        return new CheckoutResponse(
                savedEquipmentTransaction.getId().toString(),
                savedEquipmentTransaction.getEquipment().getEquipmentCode(),
                savedEquipmentTransaction.getCheckoutAt(),
                savedEquipmentTransaction.getEquipment().getStatus()
        );

    }

    public CheckinResponse  checkIn(UUID equipmentId, UUID operatorId, EquipmentCondition condition,String notes) {
        //Find Equipment
        Equipment equipment = equipmentRepository.findById(equipmentId).orElseThrow(() -> new RuntimeException("Equipment not found"));
        //find the active transaction for the equipment,
        EquipmentTransaction transaction = equipmentTransactionRepository
                .findByEquipmentAndStatus(equipment,TransactionStatus.CHECKED_OUT)
                .orElseThrow(() -> new RuntimeException("No active checkout found for this equipment"));
        //verify that it the operator that checkout that checking the equipment
        if (!transaction.getOperator().getId().equals(operatorId)){
            throw new RuntimeException("Only the operator who checked out this equipment can check it in");
        }
        // calculate duration,
        LocalDateTime checkinAt = LocalDateTime.now();
        long duration = ChronoUnit.MINUTES.between(transaction.getCheckoutAt(),checkinAt);
        // update condition,
        transaction.setConditionNotes(notes);
        transaction.setCondition(condition);
        //Equipment status
        if (condition == EquipmentCondition.GOOD){
            equipment.setStatus(EquipmentStatus.AVAILABLE);
        }else {
            equipment.setStatus(EquipmentStatus.MAINTENANCE);
        }
        equipmentRepository.save(equipment);

    //update transaction and save
        transaction.setCheckinAt(checkinAt);
        transaction.setDurationMinutes((int)duration);
        transaction.setStatus(TransactionStatus.CHECKED_IN);
        EquipmentTransaction saved = equipmentTransactionRepository.save(transaction);

        return new CheckinResponse(
                saved.getId().toString(),
                saved.getEquipment().getEquipmentCode(),
                saved.getCheckinAt(),
                saved.getDurationMinutes(),
                saved.getCondition()
        );

    }


}

