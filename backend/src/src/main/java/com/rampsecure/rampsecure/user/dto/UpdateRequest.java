package com.rampsecure.rampsecure.user.dto;

import com.rampsecure.rampsecure.user.model.Role;
import com.rampsecure.rampsecure.user.model.Station;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
    private Station station;
    private Role role;

}
