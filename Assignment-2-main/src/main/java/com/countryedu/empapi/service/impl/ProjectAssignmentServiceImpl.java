package com.countryedu.empapi.service.impl;

import com.countryedu.empapi.dto.AssignmentRequestDTO;
import com.countryedu.empapi.dto.AssignmentResponseDTO;
import com.countryedu.empapi.entity.*;
import com.countryedu.empapi.exception.BusinessRuleException;
import com.countryedu.empapi.exception.ResourceNotFoundException;
import com.countryedu.empapi.repository.EmployeeRepository;
import com.countryedu.empapi.repository.ProjectAssignmentRepository;
import com.countryedu.empapi.repository.ProjectRepository;
import com.countryedu.empapi.service.ProjectAssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProjectAssignmentServiceImpl implements ProjectAssignmentService {

    private final ProjectAssignmentRepository assignmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    public ProjectAssignmentServiceImpl(ProjectAssignmentRepository assignmentRepository,
                                        EmployeeRepository employeeRepository,
                                        ProjectRepository projectRepository) {
        this.assignmentRepository = assignmentRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public AssignmentResponseDTO assignEmployeeToProject(AssignmentRequestDTO request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        if (employee.getStatus() != EmployeeStatus.ACTIVE) {
            throw new BusinessRuleException("Cannot assign inactive employee (" + employee.getStatus() + ") to project");
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));

        if (project.getStatus() == ProjectStatus.COMPLETED || project.getStatus() == ProjectStatus.SUSPENDED) {
            throw new BusinessRuleException("Cannot assign employee to a " + project.getStatus() + " project");
        }

        // Rule 1: Prevent duplicate active assignment to same project
        boolean alreadyAssigned = assignmentRepository.existsByEmployeeIdAndProjectIdAndStatus(
                employee.getId(), project.getId(), AssignmentStatus.ACTIVE
        );
        if (alreadyAssigned) {
            throw new BusinessRuleException("Employee is already actively assigned to this project");
        }

        // Rule 2: Allocation percentage bounds check & aggregate allocation threshold
        Integer currentAllocationSum = assignmentRepository.sumActiveAllocationByEmployeeId(employee.getId());
        int totalAllocationAfter = (currentAllocationSum != null ? currentAllocationSum : 0) + request.getAllocationPercentage();
        if (totalAllocationAfter > 100) {
            throw new BusinessRuleException(
                    "Total employee workload cannot exceed 100%. Current: " +
                    (currentAllocationSum != null ? currentAllocationSum : 0) +
                    "%, Attempted addition: " + request.getAllocationPercentage() + "%"
            );
        }

        ProjectAssignment assignment = ProjectAssignment.builder()
                .employee(employee)
                .project(project)
                .roleInProject(request.getRoleInProject())
                .allocationPercentage(request.getAllocationPercentage())
                .assignedDate(request.getAssignedDate() != null ? request.getAssignedDate() : LocalDate.now())
                .status(AssignmentStatus.ACTIVE)
                .build();

        ProjectAssignment saved = assignmentRepository.save(assignment);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public AssignmentResponseDTO releaseEmployeeFromProject(Long assignmentId) {
        ProjectAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + assignmentId));

        if (assignment.getStatus() != AssignmentStatus.ACTIVE) {
            throw new BusinessRuleException("Assignment is already " + assignment.getStatus());
        }

        assignment.setStatus(AssignmentStatus.RELEASED);
        assignment.setReleaseDate(LocalDate.now());

        ProjectAssignment updated = assignmentRepository.save(assignment);
        return mapToDTO(updated);
    }

    @Override
    public List<AssignmentResponseDTO> getAssignmentsByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found with id: " + employeeId);
        }
        return assignmentRepository.findByEmployeeId(employeeId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentResponseDTO> getAssignmentsByProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        return assignmentRepository.findByProjectId(projectId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AssignmentResponseDTO mapToDTO(ProjectAssignment pa) {
        return AssignmentResponseDTO.builder()
                .id(pa.getId())
                .employeeId(pa.getEmployee().getId())
                .employeeName(pa.getEmployee().getFirstName() + " " + pa.getEmployee().getLastName())
                .employeeCode(pa.getEmployee().getEmployeeCode())
                .projectId(pa.getProject().getId())
                .projectName(pa.getProject().getName())
                .projectCode(pa.getProject().getProjectCode())
                .roleInProject(pa.getRoleInProject())
                .allocationPercentage(pa.getAllocationPercentage())
                .assignedDate(pa.getAssignedDate())
                .releaseDate(pa.getReleaseDate())
                .status(pa.getStatus())
                .version(pa.getVersion())
                .createdAt(pa.getCreatedAt())
                .build();
    }
}
