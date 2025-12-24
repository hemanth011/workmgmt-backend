package com.workmgmt.workmgmt.dto;

import lombok.Data;

@Data
public class TaskRequestDto {
    private String title;
    private String description;
    private Long assignedToUserId; // optional
}
