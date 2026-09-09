package com.countryedu.empapi.dto;

public class DepartmentHeadcountDTO {
    private Long departmentId;
    private String departmentName;
    private String departmentCode;
    private Long employeeCount;
    private Double averageSalary;
    private Double totalPayroll;

    public DepartmentHeadcountDTO() {}

    public DepartmentHeadcountDTO(Long departmentId, String departmentName, String departmentCode,
                                  Number employeeCount, Number averageSalary, Number totalPayroll) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.employeeCount = employeeCount != null ? employeeCount.longValue() : 0L;
        this.averageSalary = averageSalary != null ? averageSalary.doubleValue() : 0.0;
        this.totalPayroll = totalPayroll != null ? totalPayroll.doubleValue() : 0.0;
    }

    public DepartmentHeadcountDTO(Long departmentId, String departmentName, String departmentCode,
                                  Long employeeCount, Double averageSalary, Double totalPayroll) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.employeeCount = employeeCount;
        this.averageSalary = averageSalary;
        this.totalPayroll = totalPayroll;
    }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }
    public Long getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(Long employeeCount) { this.employeeCount = employeeCount; }
    public Double getAverageSalary() { return averageSalary; }
    public void setAverageSalary(Double averageSalary) { this.averageSalary = averageSalary; }
    public Double getTotalPayroll() { return totalPayroll; }
    public void setTotalPayroll(Double totalPayroll) { this.totalPayroll = totalPayroll; }
}
