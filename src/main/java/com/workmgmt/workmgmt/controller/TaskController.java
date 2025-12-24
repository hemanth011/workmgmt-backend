package com.workmgmt.workmgmt.controller;


import com.workmgmt.workmgmt.dto.TaskRequestDto;
import com.workmgmt.workmgmt.dto.TaskStatusUpdateRequestDto;
import com.workmgmt.workmgmt.entity.Task;
import com.workmgmt.workmgmt.enums.TaskStatus;
import com.workmgmt.workmgmt.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public Task createTask(@RequestParam Long projectId,
                           @RequestParam Long creatorId,
                           @RequestBody TaskRequestDto dto) throws Throwable {
        return taskService.createTask(projectId, dto);
    }
    @PatchMapping("/{taskId}/status")
    public Task updateStatus(@PathVariable Long taskId,
                             @RequestParam TaskStatus status) throws Throwable {
        return taskService.updateTaskStatus(taskId, status);
    }

}
