package com.rampsecure.rampsecure.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDetails {
    private UUID userId;
    private String station;
}
