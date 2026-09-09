package com.countryedu.empapi.dto;

import com.countryedu.empapi.entity.AssignmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssignmentResponseDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String employeeCode;
    private Long projectId;
    private String projectName;
    private String projectCode;
    private String roleInProject;
    private Integer allocationPercentage;
    private LocalDate assignedDate;
    private LocalDate releaseDate;
    private AssignmentStatus status;
    private Long version;
    private LocalDateTime createdAt;

    public AssignmentResponseDTO() {}

    public AssignmentResponseDTO(Long id, Long employeeId, String employeeName, String employeeCode,
                                 Long projectId, String projectName, String projectCode,
                                 String roleInProject, Integer allocationPercentage,
                                 LocalDate assignedDate, LocalDate releaseDate,
                                 AssignmentStatus status, Long version, LocalDateTime createdAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeCode = employeeCode;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectCode = projectCode;
        this.roleInProject = roleInProject;
        this.allocationPercentage = allocationPercentage;
        this.assignedDate = assignedDate;
        this.releaseDate = releaseDate;
        this.status = status;
        this.version = version;
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long employeeId;
        private String employeeName;
        private String employeeCode;
        private Long projectId;
        private String projectName;
        private String projectCode;
        private String roleInProject;
        private Integer allocationPercentage;
        private LocalDate assignedDate;
        private LocalDate releaseDate;
        private AssignmentStatus status;
        private Long version;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder employeeId(Long employeeId) { this.employeeId = employeeId; return this; }
        public Builder employeeName(String employeeName) { this.employeeName = employeeName; return this; }
        public Builder employeeCode(String employeeCode) { this.employeeCode = employeeCode; return this; }
        public Builder projectId(Long projectId) { this.projectId = projectId; return this; }
        public Builder projectName(String projectName) { this.projectName = projectName; return this; }
        public Builder projectCode(String projectCode) { this.projectCode = projectCode; return this; }
        public Builder roleInProject(String roleInProject) { this.roleInProject = roleInProject; return this; }
        public Builder allocationPercentage(Integer allocationPercentage) { this.allocationPercentage = allocationPercentage; return this; }
        public Builder assignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; return this; }
        public Builder releaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; return this; }
        public Builder status(AssignmentStatus status) { this.status = status; return this; }
        public Builder version(Long version) { this.version = version; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AssignmentResponseDTO build() {
            return new AssignmentResponseDTO(id, employeeId, employeeName, employeeCode, projectId,
                    projectName, projectCode, roleInProject, allocationPercentage, assignedDate,
                    releaseDate, status, version, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }
    public String getRoleInProject() { return roleInProject; }
    public void setRoleInProject(String roleInProject) { this.roleInProject = roleInProject; }
    public Integer getAllocationPercentage() { return allocationPercentage; }
    public void setAllocationPercentage(Integer allocationPercentage) { this.allocationPercentage = allocationPercentage; }
    public LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
