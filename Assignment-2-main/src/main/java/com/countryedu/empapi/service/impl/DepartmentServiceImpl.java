package com.countryedu.empapi.service.impl;

import com.countryedu.empapi.dto.DepartmentRequestDTO;
import com.countryedu.empapi.dto.DepartmentResponseDTO;
import com.countryedu.empapi.entity.Department;
import com.countryedu.empapi.exception.BusinessRuleException;
import com.countryedu.empapi.exception.ResourceNotFoundException;
import com.countryedu.empapi.repository.DepartmentRepository;
import com.countryedu.empapi.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return mapToDTO(department);
    }

    @Override
    @Transactional
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new BusinessRuleException("Department name " + request.getName() + " already exists");
        }
        if (departmentRepository.existsByCode(request.getCode())) {
            throw new BusinessRuleException("Department code " + request.getCode() + " already exists");
        }

        Department department = Department.builder()
                .name(request.getName())
                .code(request.getCode().toUpperCase())
                .build();

        Department saved = departmentRepository.save(department);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (!department.getName().equalsIgnoreCase(request.getName()) && departmentRepository.existsByName(request.getName())) {
            throw new BusinessRuleException("Department name " + request.getName() + " already exists");
        }
        if (!department.getCode().equalsIgnoreCase(request.getCode()) && departmentRepository.existsByCode(request.getCode())) {
            throw new BusinessRuleException("Department code " + request.getCode() + " already exists");
        }

        department.setName(request.getName());
        department.setCode(request.getCode().toUpperCase());
        Department updated = departmentRepository.save(department);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (!department.getEmployees().isEmpty()) {
            throw new BusinessRuleException("Cannot delete department with assigned employees. Reassign employees first.");
        }

        departmentRepository.delete(department);
    }

    private DepartmentResponseDTO mapToDTO(Department dept) {
        return DepartmentResponseDTO.builder()
                .id(dept.getId())
                .name(dept.getName())
                .code(dept.getCode())
                .employeeCount(dept.getEmployees() != null ? dept.getEmployees().size() : 0)
                .version(dept.getVersion())
                .createdAt(dept.getCreatedAt())
                .updatedAt(dept.getUpdatedAt())
                .build();
    }
}
