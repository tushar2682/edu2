package com.countryedu.empapi.dto;

import com.countryedu.empapi.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProjectRequestDTO {

    @NotBlank(message = "Project code is required")
    @Size(min = 2, max = 50, message = "Project code must be between 2 and 50 characters")
    private String projectCode;

    @NotBlank(message = "Project name is required")
    @Size(min = 2, max = 150, message = "Project name must be between 2 and 150 characters")
    private String name;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    private ProjectStatus status = ProjectStatus.PLANNED;

    @PositiveOrZero(message = "Budget must be greater than or equal to 0")
    private BigDecimal budget;

    private Long version;

    public ProjectRequestDTO() {}

    public ProjectRequestDTO(String projectCode, String name, String description, LocalDate startDate,
                             LocalDate endDate, ProjectStatus status, BigDecimal budget, Long version) {
        this.projectCode = projectCode;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status != null ? status : ProjectStatus.PLANNED;
        this.budget = budget;
        this.version = version;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String projectCode;
        private String name;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private ProjectStatus status = ProjectStatus.PLANNED;
        private BigDecimal budget;
        private Long version;

        public Builder projectCode(String projectCode) { this.projectCode = projectCode; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public Builder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public Builder status(ProjectStatus status) { this.status = status; return this; }
        public Builder budget(BigDecimal budget) { this.budget = budget; return this; }
        public Builder version(Long version) { this.version = version; return this; }

        public ProjectRequestDTO build() {
            return new ProjectRequestDTO(projectCode, name, description, startDate, endDate, status, budget, version);
        }
    }

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
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
