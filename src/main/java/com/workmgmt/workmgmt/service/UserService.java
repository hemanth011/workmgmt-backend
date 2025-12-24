package com.workmgmt.workmgmt.service;

import com.workmgmt.workmgmt.dto.UserRequestDto;
import com.workmgmt.workmgmt.entity.User;
import com.workmgmt.workmgmt.enums.UserRole;
import com.workmgmt.workmgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    public User createUser(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = modelMapper.map(dto, User.class);
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepository.save(user);

        // Send welcome email
        emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getName());

        return savedUser;
    }
}
