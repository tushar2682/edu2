package com.countryedu.empapi.service.impl;

import com.countryedu.empapi.dto.EmployeeRequestDTO;
import com.countryedu.empapi.dto.EmployeeResponseDTO;
import com.countryedu.empapi.entity.Department;
import com.countryedu.empapi.entity.Employee;
import com.countryedu.empapi.entity.EmployeeStatus;
import com.countryedu.empapi.exception.BusinessRuleException;
import com.countryedu.empapi.exception.ResourceNotFoundException;
import com.countryedu.empapi.repository.DepartmentRepository;
import com.countryedu.empapi.repository.EmployeeRepository;
import com.countryedu.empapi.service.EmployeeService;
import com.countryedu.empapi.specification.EmployeeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Page<EmployeeResponseDTO> searchEmployees(
            Long departmentId,
            EmployeeStatus status,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            String searchTerm,
            Pageable pageable
    ) {
        Specification<Employee> spec = EmployeeSpecification.filterByCriteria(
                departmentId, status, minSalary, maxSalary, searchTerm
        );
        return employeeRepository.findAll(spec, pageable).map(this::mapToDTO);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return mapToDTO(employee);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO request) {
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new BusinessRuleException("Employee with code " + request.getEmployeeCode() + " already exists");
        }
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Employee with email " + request.getEmail() + " already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));

        Employee employee = Employee.builder()
                .employeeCode(request.getEmployeeCode())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .jobTitle(request.getJobTitle())
                .salary(request.getSalary())
                .hireDate(request.getHireDate())
                .status(request.getStatus() != null ? request.getStatus() : EmployeeStatus.ACTIVE)
                .department(department)
                .build();

        Employee saved = employeeRepository.save(employee);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // Validate optimistic lock version if provided
        if (request.getVersion() != null && !employee.getVersion().equals(request.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Employee.class, id);
        }

        // Check unique constraints if fields modified
        if (!employee.getEmail().equalsIgnoreCase(request.getEmail()) && employeeRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Employee with email " + request.getEmail() + " already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setJobTitle(request.getJobTitle());
        employee.setSalary(request.getSalary());
        employee.setHireDate(request.getHireDate());
        if (request.getStatus() != null) {
            employee.setStatus(request.getStatus());
        }
        employee.setDepartment(department);

        Employee updated = employeeRepository.save(employee);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO transferDepartment(Long id, Long newDepartmentId, Long version) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (version != null && !employee.getVersion().equals(version)) {
            throw new ObjectOptimisticLockingFailureException(Employee.class, id);
        }

        Department newDept = departmentRepository.findById(newDepartmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + newDepartmentId));

        employee.setDepartment(newDept);
        Employee updated = employeeRepository.save(employee);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateSalary(Long id, BigDecimal newSalary, Long version) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (version != null && !employee.getVersion().equals(version)) {
            throw new ObjectOptimisticLockingFailureException(Employee.class, id);
        }

        if (newSalary.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Salary cannot be negative");
        }

        employee.setSalary(newSalary);
        Employee updated = employeeRepository.save(employee);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
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
