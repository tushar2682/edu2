package com.countryedu.empapi.service;

import com.countryedu.empapi.dto.AssignmentRequestDTO;
import com.countryedu.empapi.dto.AssignmentResponseDTO;
import com.countryedu.empapi.entity.*;
import com.countryedu.empapi.exception.BusinessRuleException;
import com.countryedu.empapi.repository.EmployeeRepository;
import com.countryedu.empapi.repository.ProjectAssignmentRepository;
import com.countryedu.empapi.repository.ProjectRepository;
import com.countryedu.empapi.service.impl.ProjectAssignmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectAssignmentServiceTest {

    @Mock
    private ProjectAssignmentRepository assignmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectAssignmentServiceImpl assignmentService;

    private Employee employee;
    private Project project;

    @BeforeEach
    void setUp() {
        Department dept = Department.builder().id(1L).name("Engineering").code("ENG").build();
        employee = Employee.builder()
                .id(1L)
                .employeeCode("EMP001")
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice@countryedu.com")
                .jobTitle("Developer")
                .salary(BigDecimal.valueOf(90000))
                .status(EmployeeStatus.ACTIVE)
                .department(dept)
                .version(0L)
                .build();

        project = Project.builder()
                .id(1L)
                .projectCode("PRJ-001")
                .name("Cloud Modernization")
                .status(ProjectStatus.ACTIVE)
                .startDate(LocalDate.now())
                .version(0L)
                .build();
    }

    @Test
    @DisplayName("Should successfully assign employee when under 100% capacity and not duplicate")
    void shouldSuccessfullyAssignEmployee() {
        AssignmentRequestDTO request = AssignmentRequestDTO.builder()
                .employeeId(1L)
                .projectId(1L)
                .roleInProject("Tech Lead")
                .allocationPercentage(50)
                .assignedDate(LocalDate.now())
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(assignmentRepository.existsByEmployeeIdAndProjectIdAndStatus(1L, 1L, AssignmentStatus.ACTIVE)).thenReturn(false);
        when(assignmentRepository.sumActiveAllocationByEmployeeId(1L)).thenReturn(30);

        ProjectAssignment saved = ProjectAssignment.builder()
                .id(10L)
                .employee(employee)
                .project(project)
                .roleInProject("Tech Lead")
                .allocationPercentage(50)
                .assignedDate(LocalDate.now())
                .status(AssignmentStatus.ACTIVE)
                .version(0L)
                .build();

        when(assignmentRepository.save(any(ProjectAssignment.class))).thenReturn(saved);

        AssignmentResponseDTO response = assignmentService.assignEmployeeToProject(request);

        assertThat(response).isNotNull();
        assertThat(response.getAllocationPercentage()).isEqualTo(50);
        assertThat(response.getRoleInProject()).isEqualTo("Tech Lead");
        verify(assignmentRepository).save(any(ProjectAssignment.class));
    }

    @Test
    @DisplayName("Should reject assignment when employee is already actively assigned to the project")
    void shouldRejectDuplicateActiveAssignment() {
        AssignmentRequestDTO request = AssignmentRequestDTO.builder()
                .employeeId(1L)
                .projectId(1L)
                .roleInProject("Developer")
                .allocationPercentage(40)
                .assignedDate(LocalDate.now())
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(assignmentRepository.existsByEmployeeIdAndProjectIdAndStatus(1L, 1L, AssignmentStatus.ACTIVE)).thenReturn(true);

        assertThatThrownBy(() -> assignmentService.assignEmployeeToProject(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("already actively assigned");

        verify(assignmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should reject assignment when total employee allocation would exceed 100%")
    void shouldRejectAllocationExceedingCapacity() {
        AssignmentRequestDTO request = AssignmentRequestDTO.builder()
                .employeeId(1L)
                .projectId(1L)
                .roleInProject("Developer")
                .allocationPercentage(60)
                .assignedDate(LocalDate.now())
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(assignmentRepository.existsByEmployeeIdAndProjectIdAndStatus(1L, 1L, AssignmentStatus.ACTIVE)).thenReturn(false);
        when(assignmentRepository.sumActiveAllocationByEmployeeId(1L)).thenReturn(50); // 50 + 60 = 110 > 100

        assertThatThrownBy(() -> assignmentService.assignEmployeeToProject(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Total employee workload cannot exceed 100%");

        verify(assignmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should reject assignment when project is COMPLETED")
    void shouldRejectAssignmentToCompletedProject() {
        project.setStatus(ProjectStatus.COMPLETED);

        AssignmentRequestDTO request = AssignmentRequestDTO.builder()
                .employeeId(1L)
                .projectId(1L)
                .roleInProject("Developer")
                .allocationPercentage(20)
                .assignedDate(LocalDate.now())
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertThatThrownBy(() -> assignmentService.assignEmployeeToProject(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Cannot assign employee to a COMPLETED project");

        verify(assignmentRepository, never()).save(any());
    }
}
