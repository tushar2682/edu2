package com.countryedu.empapi.service.impl;

import com.countryedu.empapi.dto.ProjectRequestDTO;
import com.countryedu.empapi.dto.ProjectResponseDTO;
import com.countryedu.empapi.entity.Project;
import com.countryedu.empapi.exception.BusinessRuleException;
import com.countryedu.empapi.exception.ResourceNotFoundException;
import com.countryedu.empapi.repository.ProjectRepository;
import com.countryedu.empapi.service.ProjectService;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return mapToDTO(project);
    }

    @Override
    @Transactional
    public ProjectResponseDTO createProject(ProjectRequestDTO request) {
        if (projectRepository.existsByProjectCode(request.getProjectCode())) {
            throw new BusinessRuleException("Project with code " + request.getProjectCode() + " already exists");
        }
        if (request.getEndDate() != null && request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessRuleException("End date cannot be earlier than start date");
        }

        Project project = Project.builder()
                .projectCode(request.getProjectCode())
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .budget(request.getBudget())
                .build();

        Project saved = projectRepository.save(project);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if (request.getVersion() != null && !project.getVersion().equals(request.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Project.class, id);
        }

        if (request.getEndDate() != null && request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessRuleException("End date cannot be earlier than start date");
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }
        project.setBudget(request.getBudget());

        Project updated = projectRepository.save(project);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        projectRepository.delete(project);
    }

    private ProjectResponseDTO mapToDTO(Project p) {
        return ProjectResponseDTO.builder()
                .id(p.getId())
                .projectCode(p.getProjectCode())
                .name(p.getName())
                .description(p.getDescription())
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .status(p.getStatus())
                .budget(p.getBudget())
                .assignedEmployeesCount(p.getAssignments() != null ? p.getAssignments().size() : 0)
                .version(p.getVersion())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
