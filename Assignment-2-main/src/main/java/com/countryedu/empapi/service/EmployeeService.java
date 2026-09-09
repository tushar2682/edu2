package com.countryedu.empapi.service;

import com.countryedu.empapi.dto.EmployeeRequestDTO;
import com.countryedu.empapi.dto.EmployeeResponseDTO;
import com.countryedu.empapi.entity.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface EmployeeService {

    Page<EmployeeResponseDTO> searchEmployees(
            Long departmentId,
            EmployeeStatus status,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            String searchTerm,
            Pageable pageable
    );

    EmployeeResponseDTO getEmployeeById(Long id);

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO request);

    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO request);

    EmployeeResponseDTO transferDepartment(Long id, Long newDepartmentId, Long version);

    EmployeeResponseDTO updateSalary(Long id, BigDecimal newSalary, Long version);

    void deleteEmployee(Long id);
}
