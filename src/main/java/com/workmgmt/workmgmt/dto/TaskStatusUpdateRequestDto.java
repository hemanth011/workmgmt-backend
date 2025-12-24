package com.workmgmt.workmgmt.dto;

import com.workmgmt.workmgmt.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskStatusUpdateRequestDto {
    private TaskStatus status;
}
