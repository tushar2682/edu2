package com.countryedu.empapi.service;

import com.countryedu.empapi.dto.AssignmentRequestDTO;
import com.countryedu.empapi.dto.AssignmentResponseDTO;

import java.util.List;

public interface ProjectAssignmentService {
    AssignmentResponseDTO assignEmployeeToProject(AssignmentRequestDTO request);
    AssignmentResponseDTO releaseEmployeeFromProject(Long assignmentId);
    List<AssignmentResponseDTO> getAssignmentsByEmployee(Long employeeId);
    List<AssignmentResponseDTO> getAssignmentsByProject(Long projectId);
}
