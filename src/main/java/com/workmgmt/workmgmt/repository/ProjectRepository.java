package com.workmgmt.workmgmt.repository;

import com.workmgmt.workmgmt.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
