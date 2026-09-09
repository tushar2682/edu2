package com.countryedu.empapi.dto;

import com.countryedu.empapi.entity.ProjectStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProjectResponseDTO {
    private Long id;
    private String projectCode;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private BigDecimal budget;
    private int assignedEmployeesCount;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProjectResponseDTO() {}

    public ProjectResponseDTO(Long id, String projectCode, String name, String description,
                              LocalDate startDate, LocalDate endDate, ProjectStatus status,
                              BigDecimal budget, int assignedEmployeesCount, Long version,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.projectCode = projectCode;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.budget = budget;
        this.assignedEmployeesCount = assignedEmployeesCount;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String projectCode;
        private String name;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private ProjectStatus status;
        private BigDecimal budget;
        private int assignedEmployeesCount;
        private Long version;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder projectCode(String projectCode) { this.projectCode = projectCode; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public Builder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public Builder status(ProjectStatus status) { this.status = status; return this; }
        public Builder budget(BigDecimal budget) { this.budget = budget; return this; }
        public Builder assignedEmployeesCount(int assignedEmployeesCount) { this.assignedEmployeesCount = assignedEmployeesCount; return this; }
        public Builder version(Long version) { this.version = version; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ProjectResponseDTO build() {
            return new ProjectResponseDTO(id, projectCode, name, description, startDate, endDate,
                    status, budget, assignedEmployeesCount, version, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public int getAssignedEmployeesCount() { return assignedEmployeesCount; }
    public void setAssignedEmployeesCount(int assignedEmployeesCount) { this.assignedEmployeesCount = assignedEmployeesCount; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
