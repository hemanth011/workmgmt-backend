package com.workmgmt.workmgmt.repository;

import com.workmgmt.workmgmt.entity.Project;
import com.workmgmt.workmgmt.entity.ProjectMember;
import com.workmgmt.workmgmt.entity.User;
import com.workmgmt.workmgmt.enums.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    Optional<ProjectMember> findByProjectAndUser(Project project, User user);

    Optional<ProjectMember> findByProjectAndUserAndMemberRole(
            Project project, User user, MemberRole memberRole
    );
}
