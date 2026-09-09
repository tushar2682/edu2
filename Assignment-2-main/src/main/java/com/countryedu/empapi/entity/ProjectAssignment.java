package com.countryedu.empapi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_assignments",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_emp_proj_assigned_date", columnNames = {"employee_id", "project_id", "assigned_date"})
        },
        indexes = {
                @Index(name = "idx_assignment_emp_status", columnList = "employee_id, status"),
                @Index(name = "idx_assignment_proj_status", columnList = "project_id, status")
        }
)
public class ProjectAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "role_in_project", nullable = false, length = 50)
    private String roleInProject;

    @Column(name = "allocation_percentage", nullable = false)
    private Integer allocationPercentage;

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssignmentStatus status = AssignmentStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(nullable = false)
    private Long version = 0L;

    public ProjectAssignment() {}

    public ProjectAssignment(Long id, Employee employee, Project project, String roleInProject,
                             Integer allocationPercentage, LocalDate assignedDate,
                             LocalDate releaseDate, AssignmentStatus status,
                             LocalDateTime createdAt, LocalDateTime updatedAt, Long version) {
        this.id = id;
        this.employee = employee;
        this.project = project;
        this.roleInProject = roleInProject;
        this.allocationPercentage = allocationPercentage;
        this.assignedDate = assignedDate;
        this.releaseDate = releaseDate;
        this.status = status != null ? status : AssignmentStatus.ACTIVE;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version != null ? version : 0L;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Employee employee;
        private Project project;
        private String roleInProject;
        private Integer allocationPercentage;
        private LocalDate assignedDate;
        private LocalDate releaseDate;
        private AssignmentStatus status = AssignmentStatus.ACTIVE;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long version = 0L;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder employee(Employee employee) { this.employee = employee; return this; }
        public Builder project(Project project) { this.project = project; return this; }
        public Builder roleInProject(String roleInProject) { this.roleInProject = roleInProject; return this; }
        public Builder allocationPercentage(Integer allocationPercentage) { this.allocationPercentage = allocationPercentage; return this; }
        public Builder assignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; return this; }
        public Builder releaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; return this; }
        public Builder status(AssignmentStatus status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder version(Long version) { this.version = version; return this; }

        public ProjectAssignment build() {
            return new ProjectAssignment(id, employee, project, roleInProject, allocationPercentage,
                    assignedDate, releaseDate, status, createdAt, updatedAt, version);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
