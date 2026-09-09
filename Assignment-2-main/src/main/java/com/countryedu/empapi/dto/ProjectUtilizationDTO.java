package com.countryedu.empapi.dto;

import com.countryedu.empapi.entity.ProjectStatus;

import java.math.BigDecimal;

public class ProjectUtilizationDTO {
    private Long projectId;
    private String projectCode;
    private String projectName;
    private ProjectStatus status;
    private BigDecimal budget;
    private Long activeResourceCount;
    private Long totalAllocationPercentage;

    public ProjectUtilizationDTO() {}

    public ProjectUtilizationDTO(Long projectId, String projectCode, String projectName,
                                 ProjectStatus status, BigDecimal budget, Number activeResourceCount,
                                 Number totalAllocationPercentage) {
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.status = status;
        this.budget = budget;
        this.activeResourceCount = activeResourceCount != null ? activeResourceCount.longValue() : 0L;
        this.totalAllocationPercentage = totalAllocationPercentage != null ? totalAllocationPercentage.longValue() : 0L;
    }

    public ProjectUtilizationDTO(Long projectId, String projectCode, String projectName,
                                 ProjectStatus status, BigDecimal budget, Long activeResourceCount,
                                 Integer totalAllocationPercentage) {
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.status = status;
        this.budget = budget;
        this.activeResourceCount = activeResourceCount;
        this.totalAllocationPercentage = totalAllocationPercentage != null ? totalAllocationPercentage.longValue() : 0L;
    }

    public ProjectUtilizationDTO(Long projectId, String projectCode, String projectName,
                                 ProjectStatus status, BigDecimal budget, Long activeResourceCount,
                                 Long totalAllocationPercentage) {
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.status = status;
        this.budget = budget;
        this.activeResourceCount = activeResourceCount;
        this.totalAllocationPercentage = totalAllocationPercentage;
    }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public Long getActiveResourceCount() { return activeResourceCount; }
    public void setActiveResourceCount(Long activeResourceCount) { this.activeResourceCount = activeResourceCount; }
    public Long getTotalAllocationPercentage() { return totalAllocationPercentage; }
    public void setTotalAllocationPercentage(Long totalAllocationPercentage) { this.totalAllocationPercentage = totalAllocationPercentage; }
}
