package com.countryedu.empapi.controller;

import com.countryedu.empapi.dto.AssignmentRequestDTO;
import com.countryedu.empapi.dto.AssignmentResponseDTO;
import com.countryedu.empapi.dto.ProjectRequestDTO;
import com.countryedu.empapi.dto.ProjectResponseDTO;
import com.countryedu.empapi.service.ProjectAssignmentService;
import com.countryedu.empapi.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Projects & Assignments", description = "Project staffing, allocation constraints, and capacity controls")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectAssignmentService assignmentService;

    public ProjectController(ProjectService projectService, ProjectAssignmentService assignmentService) {
        this.projectService = projectService;
        this.assignmentService = assignmentService;
    }

    @GetMapping
    @Operation(summary = "Get all projects")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project details by ID")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Create project")
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO request) {
        return new ResponseEntity<>(projectService.createProject(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Update project with optimistic locking")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestDTO request
    ) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete project")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Assign employee to project with duplicate & capacity limit validation")
    public ResponseEntity<AssignmentResponseDTO> assignEmployee(@Valid @RequestBody AssignmentRequestDTO request) {
        return new ResponseEntity<>(assignmentService.assignEmployeeToProject(request), HttpStatus.CREATED);
    }

    @PutMapping("/assignments/{assignmentId}/release")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Release employee from project assignment")
    public ResponseEntity<AssignmentResponseDTO> releaseEmployee(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.releaseEmployeeFromProject(assignmentId));
    }

    @GetMapping("/{projectId}/assignments")
    @Operation(summary = "Get all assignments for a specific project")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByProject(projectId));
    }

    @GetMapping("/employee/{employeeId}/assignments")
    @Operation(summary = "Get all project assignments for an employee")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByEmployee(employeeId));
    }
}
