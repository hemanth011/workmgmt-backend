package com.workmgmt.workmgmt.entity;

import com.workmgmt.workmgmt.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    private Project project;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User assignedTo;

//    @Version
//    private Long version;
}