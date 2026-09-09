package com.countryedu.empapi.controller;

import com.countryedu.empapi.dto.DepartmentHeadcountDTO;
import com.countryedu.empapi.dto.EmployeeResponseDTO;
import com.countryedu.empapi.dto.ProjectUtilizationDTO;
import com.countryedu.empapi.service.ReportingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reporting & Analytics", description = "Aggregated organizational and resource analytics")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class ReportController {

    private final ReportingService reportingService;

    public ReportController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/department-headcount")
    @Operation(summary = "Get headcount, average salary, and payroll totals grouped by department")
    public ResponseEntity<List<DepartmentHeadcountDTO>> getDepartmentHeadcounts() {
        return ResponseEntity.ok(reportingService.getDepartmentHeadcounts());
    }

    @GetMapping("/project-utilization")
    @Operation(summary = "Get resource utilization and active staff count per project")
    public ResponseEntity<List<ProjectUtilizationDTO>> getProjectUtilization() {
        return ResponseEntity.ok(reportingService.getProjectUtilizationSummary());
    }

    @GetMapping("/underutilized-staff")
    @Operation(summary = "List all active employees with under 100% project allocation")
    public ResponseEntity<List<EmployeeResponseDTO>> getUnderutilizedEmployees() {
        return ResponseEntity.ok(reportingService.getUnderutilizedEmployees());
    }
}
