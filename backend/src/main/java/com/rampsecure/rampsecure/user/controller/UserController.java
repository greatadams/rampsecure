package com.rampsecure.rampsecure.user.controller;

import com.rampsecure.rampsecure.user.dto.UpdateRequest;
import com.rampsecure.rampsecure.user.dto.UserResponse;
import com.rampsecure.rampsecure.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
     private final UserService userService;

 //get all users
    @GetMapping("/allusers")
    public List<UserResponse> findAllUsers() {
        return userService.getAllUsers();
    }


// update role/station
    @PutMapping("/{id}")
    public UserResponse updateUser(
             @PathVariable UUID id,
             @RequestBody UpdateRequest request) {
                return userService.updateUser(id,request.getRole(),request.getStation());
            }

 //delete users
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") UUID id) {
                userService.deleteUser(id);

    }

}
