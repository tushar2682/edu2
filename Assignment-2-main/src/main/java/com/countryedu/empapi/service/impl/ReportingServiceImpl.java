package com.countryedu.empapi.service.impl;

import com.countryedu.empapi.dto.DepartmentHeadcountDTO;
import com.countryedu.empapi.dto.EmployeeResponseDTO;
import com.countryedu.empapi.dto.ProjectUtilizationDTO;
import com.countryedu.empapi.entity.Employee;
import com.countryedu.empapi.entity.EmployeeStatus;
import com.countryedu.empapi.repository.EmployeeRepository;
import com.countryedu.empapi.repository.ProjectAssignmentRepository;
import com.countryedu.empapi.service.ReportingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReportingServiceImpl implements ReportingService {

    private final EmployeeRepository employeeRepository;
    private final ProjectAssignmentRepository projectAssignmentRepository;

    public ReportingServiceImpl(EmployeeRepository employeeRepository,
                                ProjectAssignmentRepository projectAssignmentRepository) {
        this.employeeRepository = employeeRepository;
        this.projectAssignmentRepository = projectAssignmentRepository;
    }

    @Override
    public List<DepartmentHeadcountDTO> getDepartmentHeadcounts() {
        return employeeRepository.getDepartmentHeadcounts();
    }

    @Override
    public List<ProjectUtilizationDTO> getProjectUtilizationSummary() {
        return projectAssignmentRepository.getProjectUtilizationSummary();
    }

    @Override
    public List<EmployeeResponseDTO> getUnderutilizedEmployees() {
        return employeeRepository.findAll().stream()
                .filter(emp -> emp.getStatus() == EmployeeStatus.ACTIVE)
                .filter(emp -> {
                    Integer total = projectAssignmentRepository.sumActiveAllocationByEmployeeId(emp.getId());
                    return total == null || total < 100;
                })
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private EmployeeResponseDTO mapToDTO(Employee employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .fullName(employee.getFirstName() + " " + employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .jobTitle(employee.getJobTitle())
                .salary(employee.getSalary())
                .hireDate(employee.getHireDate())
                .status(employee.getStatus())
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                .departmentCode(employee.getDepartment() != null ? employee.getDepartment().getCode() : null)
                .version(employee.getVersion())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}
