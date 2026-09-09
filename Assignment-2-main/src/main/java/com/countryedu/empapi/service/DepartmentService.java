package com.countryedu.empapi.service;

import com.countryedu.empapi.dto.DepartmentRequestDTO;
import com.countryedu.empapi.dto.DepartmentResponseDTO;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponseDTO> getAllDepartments();
    DepartmentResponseDTO getDepartmentById(Long id);
    DepartmentResponseDTO createDepartment(DepartmentRequestDTO request);
    DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request);
    void deleteDepartment(Long id);
}
