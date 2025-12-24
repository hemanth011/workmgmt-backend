package com.workmgmt.workmgmt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskStatusEvent {
    private Long taskId;
    private String status;
}
