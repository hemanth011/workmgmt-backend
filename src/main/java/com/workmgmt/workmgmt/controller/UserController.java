package com.workmgmt.workmgmt.controller;

import com.workmgmt.workmgmt.dto.UserRequestDto;
import com.workmgmt.workmgmt.entity.User;
import com.workmgmt.workmgmt.enums.UserRole;
import com.workmgmt.workmgmt.repository.UserRepository;
import com.workmgmt.workmgmt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserRequestDto dto) {
        return userService.createUser(dto);
    }
}
