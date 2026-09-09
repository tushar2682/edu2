package com.countryedu.empapi.service;

import com.countryedu.empapi.dto.ProjectRequestDTO;
import com.countryedu.empapi.dto.ProjectResponseDTO;

import java.util.List;

public interface ProjectService {
    List<ProjectResponseDTO> getAllProjects();
    ProjectResponseDTO getProjectById(Long id);
    ProjectResponseDTO createProject(ProjectRequestDTO request);
    ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request);
    void deleteProject(Long id);
}
