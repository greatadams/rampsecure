package com.rampsecure.rampsecure.equipment.controller;

import com.rampsecure.rampsecure.equipment.dto.CheckInRequest;
import com.rampsecure.rampsecure.equipment.dto.CheckinResponse;
import com.rampsecure.rampsecure.equipment.dto.CheckoutRequest;
import com.rampsecure.rampsecure.equipment.dto.CheckoutResponse;
import com.rampsecure.rampsecure.equipment.service.CheckoutService;
import com.rampsecure.rampsecure.security.AuthDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;


    @PostMapping("/checkout")
    public CheckoutResponse checkOut(@Valid @RequestBody CheckoutRequest request ) {
        //extract ID from the security context because they logged in the ID is already in the JWT token
        var auth = SecurityContextHolder.getContext().getAuthentication();
        AuthDetails details = (AuthDetails) auth.getDetails();
        UUID operatorId = details.getUserId();
        return checkoutService.checkOut(request.getEquipmentId(), operatorId);

    }

    @PostMapping("/checkin")
    public CheckinResponse checkIn(@Valid @RequestBody CheckInRequest request ) {
        //extract ID from the security context because they logged in the ID is already in the JWT token
        var auth = SecurityContextHolder.getContext().getAuthentication();
        AuthDetails details = (AuthDetails) auth.getDetails();
        UUID operatorId = details.getUserId();
        return checkoutService.checkIn(request.getEquipmentId(),operatorId,request.getCondition(),request.getNotes());

    }

}
