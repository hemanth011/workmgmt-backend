package com.workmgmt.workmgmt.service;

import com.workmgmt.workmgmt.dto.TaskRequestDto;
import com.workmgmt.workmgmt.entity.Project;
import com.workmgmt.workmgmt.entity.Task;
import com.workmgmt.workmgmt.entity.TaskStatusEvent;
import com.workmgmt.workmgmt.entity.User;
import com.workmgmt.workmgmt.enums.TaskStatus;
import com.workmgmt.workmgmt.exception.BadRequestException;
import com.workmgmt.workmgmt.exception.NotFoundException;
import com.workmgmt.workmgmt.repository.ProjectMemberRepository;
import com.workmgmt.workmgmt.repository.ProjectRepository;
import com.workmgmt.workmgmt.repository.TaskRepository;
import com.workmgmt.workmgmt.repository.UserRepository;
import com.workmgmt.workmgmt.security.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SimpMessagingTemplate messagingTemplate;

    // ---------------- CREATE TASK ----------------

    @Transactional
    public Task createTask(Long projectId, TaskRequestDto dto) throws Throwable {

        String email = SecurityUtil.getCurrentUserEmail();

        User creator = (User) userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Logged-in user not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        projectMemberRepository.findByProjectAndUser(project, creator)
                .orElseThrow(() -> new BadRequestException("User is not a project member"));

        Task task = modelMapper.map(dto, Task.class);
        task.setId(null); // important
        task.setProject(project);
        task.setCreatedBy(creator);
        task.setStatus(TaskStatus.TODO);

        if (dto.getAssignedToUserId() != null) {
            User assignee = userRepository.findById(dto.getAssignedToUserId())
                    .orElseThrow(() -> new NotFoundException("Assigned user not found"));

            projectMemberRepository.findByProjectAndUser(project, assignee)
                    .orElseThrow(() -> new BadRequestException("Assignee not in project"));

            task.setAssignedTo(assignee);
        }

        return taskRepository.save(task);
    }

    // ---------------- UPDATE TASK STATUS ----------------

    public Task updateTaskStatus(Long taskId, TaskStatus status) throws Throwable {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = (User) userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Logged-in user not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        projectMemberRepository.findByProjectAndUser(task.getProject(), user)
                .orElseThrow(() -> new BadRequestException("User is not a project member"));

        task.setStatus(status);
        Task updated = taskRepository.save(task);

        messagingTemplate.convertAndSend(
                "/topic/tasks",
                new TaskStatusEvent(updated.getId(), updated.getStatus().name())
        );

        return updated;
    }
}
