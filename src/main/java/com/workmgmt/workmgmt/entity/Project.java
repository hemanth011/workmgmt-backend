package com.workmgmt.workmgmt.entity;


import com.workmgmt.workmgmt.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ManyToOne
    private User createdBy;
}
