package com.workmgmt.workmgmt.controller;


import com.workmgmt.workmgmt.dto.ProjectRequestDto;
import com.workmgmt.workmgmt.entity.Project;
import com.workmgmt.workmgmt.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public Project createProject(@RequestParam Long userId,
                                 @RequestBody ProjectRequestDto dto) {
        return projectService.createProject( dto);
    }
    @PostMapping("/{projectId}/members")
    public String addMember(@PathVariable Long projectId,
                            @RequestParam Long ownerId,
                            @RequestParam Long userId) {

        projectService.addMember(projectId, ownerId);
        return "Member added successfully";
    }

}
