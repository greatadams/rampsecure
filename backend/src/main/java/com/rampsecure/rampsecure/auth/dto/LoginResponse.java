package com.rampsecure.rampsecure.auth.dto;

import com.rampsecure.rampsecure.user.model.Role;
import com.rampsecure.rampsecure.user.model.Station;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private Role role;
    private Station station;
    private LocalDateTime expiryDate;

}
