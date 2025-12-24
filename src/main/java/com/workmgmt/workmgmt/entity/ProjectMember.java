package com.workmgmt.workmgmt.entity;


import com.workmgmt.workmgmt.enums.MemberRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Project project;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;
}
