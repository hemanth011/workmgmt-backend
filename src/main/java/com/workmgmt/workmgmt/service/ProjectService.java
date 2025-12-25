package com.workmgmt.workmgmt.service;

import com.workmgmt.workmgmt.dto.ProjectRequestDto;
import com.workmgmt.workmgmt.entity.Project;
import com.workmgmt.workmgmt.entity.ProjectMember;
import com.workmgmt.workmgmt.entity.User;
import com.workmgmt.workmgmt.enums.MemberRole;
import com.workmgmt.workmgmt.enums.ProjectStatus;
import com.workmgmt.workmgmt.exception.BadRequestException;
import com.workmgmt.workmgmt.exception.NotFoundException;
import com.workmgmt.workmgmt.repository.ProjectMemberRepository;
import com.workmgmt.workmgmt.repository.ProjectRepository;
import com.workmgmt.workmgmt.repository.UserRepository;
import com.workmgmt.workmgmt.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    // ---------------- CREATE PROJECT ----------------

    public Project createProject(ProjectRequestDto dto) {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Logged-in user not found"));

        Project project = modelMapper.map(dto, Project.class);
        project.setStatus(ProjectStatus.ACTIVE);
        project.setCreatedBy(user);

        Project savedProject = projectRepository.save(project);

        ProjectMember owner = new ProjectMember();
        owner.setUser(user);
        owner.setProject(savedProject);
        owner.setMemberRole(MemberRole.OWNER);

        projectMemberRepository.save(owner);

        return savedProject;
    }


    // ---------------- ADD MEMBER ----------------

    public void addMember(Long projectId, Long userIdToAdd) {

        String email = SecurityUtil.getCurrentUserEmail();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Logged-in user not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        // OWNER check
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
