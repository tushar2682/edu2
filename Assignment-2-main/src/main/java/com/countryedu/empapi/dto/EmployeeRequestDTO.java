package com.countryedu.empapi.dto;

import com.countryedu.empapi.entity.EmployeeStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeRequestDTO {

    @NotBlank(message = "Employee code is required")
    @Size(min = 2, max = 50, message = "Employee code must be between 2 and 50 characters")
    private String employeeCode;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a well-formed email address")
    private String email;

    private String phone;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotNull(message = "Salary is required")
    @PositiveOrZero(message = "Salary must be greater than or equal to 0")
    private BigDecimal salary;

    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date cannot be in the future")
    private LocalDate hireDate;

    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    private Long version;

    public EmployeeRequestDTO() {}

    public EmployeeRequestDTO(String employeeCode, String firstName, String lastName, String email,
                              String phone, String jobTitle, BigDecimal salary, LocalDate hireDate,
                              EmployeeStatus status, Long departmentId, Long version) {
        this.employeeCode = employeeCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.hireDate = hireDate;
        this.status = status != null ? status : EmployeeStatus.ACTIVE;
        this.departmentId = departmentId;
        this.version = version;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String employeeCode;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String jobTitle;
        private BigDecimal salary;
        private LocalDate hireDate;
        private EmployeeStatus status = EmployeeStatus.ACTIVE;
        private Long departmentId;
        private Long version;

        public Builder employeeCode(String employeeCode) { this.employeeCode = employeeCode; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public Builder salary(BigDecimal salary) { this.salary = salary; return this; }
        public Builder hireDate(LocalDate hireDate) { this.hireDate = hireDate; return this; }
        public Builder status(EmployeeStatus status) { this.status = status; return this; }
        public Builder departmentId(Long departmentId) { this.departmentId = departmentId; return this; }
        public Builder version(Long version) { this.version = version; return this; }

        public EmployeeRequestDTO build() {
            return new EmployeeRequestDTO(employeeCode, firstName, lastName, email, phone,
                    jobTitle, salary, hireDate, status, departmentId, version);
        }
    }

    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public EmployeeStatus getStatus() { return status; }
    public void setStatus(EmployeeStatus status) { this.status = status; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
