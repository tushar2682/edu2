package com.countryedu.empapi.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class AssignmentRequestDTO {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotBlank(message = "Role in project is required")
    @Size(min = 2, max = 50, message = "Role in project must be between 2 and 50 characters")
    private String roleInProject;

    @NotNull(message = "Allocation percentage is required")
    @Min(value = 1, message = "Allocation must be at least 1%")
    @Max(value = 100, message = "Allocation cannot exceed 100%")
    private Integer allocationPercentage;

    @NotNull(message = "Assigned date is required")
    private LocalDate assignedDate;

    public AssignmentRequestDTO() {}

    public AssignmentRequestDTO(Long employeeId, Long projectId, String roleInProject,
                                Integer allocationPercentage, LocalDate assignedDate) {
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.roleInProject = roleInProject;
        this.allocationPercentage = allocationPercentage;
        this.assignedDate = assignedDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long employeeId;
        private Long projectId;
        private String roleInProject;
        private Integer allocationPercentage;
        private LocalDate assignedDate;

        public Builder employeeId(Long employeeId) { this.employeeId = employeeId; return this; }
        public Builder projectId(Long projectId) { this.projectId = projectId; return this; }
        public Builder roleInProject(String roleInProject) { this.roleInProject = roleInProject; return this; }
        public Builder allocationPercentage(Integer allocationPercentage) { this.allocationPercentage = allocationPercentage; return this; }
        public Builder assignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; return this; }

        public AssignmentRequestDTO build() {
            return new AssignmentRequestDTO(employeeId, projectId, roleInProject, allocationPercentage, assignedDate);
        }
    }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getRoleInProject() { return roleInProject; }
    public void setRoleInProject(String roleInProject) { this.roleInProject = roleInProject; }
    public Integer getAllocationPercentage() { return allocationPercentage; }
    public void setAllocationPercentage(Integer allocationPercentage) { this.allocationPercentage = allocationPercentage; }
    public LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; }
}
