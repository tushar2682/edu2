package com.countryedu.empapi.repository;

import com.countryedu.empapi.dto.ProjectUtilizationDTO;
import com.countryedu.empapi.entity.AssignmentStatus;
import com.countryedu.empapi.entity.ProjectAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Long> {

    boolean existsByEmployeeIdAndProjectIdAndStatus(Long employeeId, Long projectId, AssignmentStatus status);

    @Query("SELECT COALESCE(SUM(pa.allocationPercentage), 0) " +
           "FROM ProjectAssignment pa " +
           "WHERE pa.employee.id = :employeeId AND pa.status = 'ACTIVE'")
    Integer sumActiveAllocationByEmployeeId(@Param("employeeId") Long employeeId);

    List<ProjectAssignment> findByEmployeeId(Long employeeId);

    List<ProjectAssignment> findByProjectId(Long projectId);

    @Query("SELECT new com.countryedu.empapi.dto.ProjectUtilizationDTO(" +
           "p.id, p.projectCode, p.name, p.status, p.budget, " +
           "COUNT(pa.id), COALESCE(SUM(pa.allocationPercentage), 0)) " +
           "FROM Project p " +
           "LEFT JOIN p.assignments pa ON pa.status = 'ACTIVE' " +
           "GROUP BY p.id, p.projectCode, p.name, p.status, p.budget")
    List<ProjectUtilizationDTO> getProjectUtilizationSummary();
}
