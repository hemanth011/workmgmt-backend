package com.workmgmt.workmgmt.repository;

import com.workmgmt.workmgmt.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
