package com.rampsecure.rampsecure.equipment.repository;

import com.rampsecure.rampsecure.equipment.model.Equipment;
import com.rampsecure.rampsecure.equipment.model.EquipmentTransaction;
import com.rampsecure.rampsecure.equipment.model.TransactionStatus;
import com.rampsecure.rampsecure.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EquipmentTransactionRepository extends JpaRepository<EquipmentTransaction, UUID> {
    Optional<EquipmentTransaction> findByEquipmentAndStatus(Equipment equipment, TransactionStatus status);
    List<EquipmentTransaction> findByOperator(User operator);
    List<EquipmentTransaction> findByEquipment(Equipment equipment);
}
