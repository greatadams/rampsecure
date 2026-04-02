package com.rampsecure.rampsecure.auth.dto;

import com.rampsecure.rampsecure.user.model.Role;
import com.rampsecure.rampsecure.user.model.Station;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username is required")

    private String username;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "First name is required")
    private String lastName;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8 , max = 25, message ="Password have to have minimum of 8 character and maximum of 25")
    private String password;

    @NotBlank(message = "Password is required")
    @Size(min = 8 , max = 25, message ="Password have to have minimum of 8 character and maximum of 25")
    private String confirmPassword;

    @NotBlank(message = "Phone_number is required")
    @Size(min = 10, max = 15, message = "Enter a valid phone number")
    private String phoneNumber;

    @NotNull(message = "Role is required")

    private Role role;

    @NotNull(message = "Station  is required")
    private Station station;
}
