package com.workmgmt.workmgmt.service;

import com.workmgmt.workmgmt.dto.UserRequestDto;
import com.workmgmt.workmgmt.entity.Project;
import com.workmgmt.workmgmt.entity.ProjectMember;
import com.workmgmt.workmgmt.entity.User;
import com.workmgmt.workmgmt.enums.MemberRole;
import com.workmgmt.workmgmt.enums.UserRole;
import com.workmgmt.workmgmt.exception.BadRequestException;
import com.workmgmt.workmgmt.exception.NotFoundException;
import com.workmgmt.workmgmt.repository.ProjectMemberRepository;
import com.workmgmt.workmgmt.repository.ProjectRepository;
import com.workmgmt.workmgmt.repository.UserRepository;
import com.workmgmt.workmgmt.security.SecurityUtil;
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
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
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

    public void addMember(Long projectId, Long userIdToAdd) {

        String email = SecurityUtil.getCurrentUserEmail();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Logged-in user not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        projectMemberRepository
                .findByProjectAndUserAndMemberRole(project, owner, MemberRole.OWNER)
                .orElseThrow(() -> new BadRequestException("Only owner can add members"));

        User userToAdd = userRepository.findById(userIdToAdd)
                .orElseThrow(() -> new NotFoundException("User to add not found"));

        if (projectMemberRepository.findByProjectAndUser(project, userToAdd).isPresent()) {
            throw new BadRequestException("User already a member of project");
        }

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(userToAdd);
        member.setMemberRole(MemberRole.MEMBER);

        projectMemberRepository.save(member);

        emailService.sendProjectAssignmentEmail(
                userToAdd.getEmail(),
                userToAdd.getName(),
                project.getName()
        );
    }

}
