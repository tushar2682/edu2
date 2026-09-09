package com.countryedu.empapi.controller;

import com.countryedu.empapi.dto.EmployeeRequestDTO;
import com.countryedu.empapi.dto.EmployeeResponseDTO;
import com.countryedu.empapi.dto.PageResponseDTO;
import com.countryedu.empapi.entity.EmployeeStatus;
import com.countryedu.empapi.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employees", description = "Employee lifecycle, dynamic search, filtering, and concurrency-safe updates")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @Operation(summary = "Search, filter, and paginate employees dynamically")
    public ResponseEntity<PageResponseDTO<EmployeeResponseDTO>> searchEmployees(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        // Enforce maximum page size guardrail
        int effectiveSize = Math.min(size, 100);

        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, effectiveSize, Sort.by(direction, sort[0]));

        Page<EmployeeResponseDTO> result = employeeService.searchEmployees(
                departmentId, status, minSalary, maxSalary, search, pageable
        );

        return ResponseEntity.ok(PageResponseDTO.fromPage(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee profile by ID")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Create a new employee")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO request) {
        EmployeeResponseDTO created = employeeService.createEmployee(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Update employee with optimistic lock version validation")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDTO request
    ) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @PatchMapping("/{id}/department")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Transfer employee to a different department")
    public ResponseEntity<EmployeeResponseDTO> transferDepartment(
            @PathVariable Long id,
            @RequestParam Long newDepartmentId,
            @RequestParam(required = false) Long version
    ) {
        return ResponseEntity.ok(employeeService.transferDepartment(id, newDepartmentId, version));
    }

    @PatchMapping("/{id}/salary")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Update employee salary with optimistic locking")
    public ResponseEntity<EmployeeResponseDTO> updateSalary(
            @PathVariable Long id,
            @RequestParam BigDecimal salary,
            @RequestParam(required = false) Long version
    ) {
        return ResponseEntity.ok(employeeService.updateSalary(id, salary, version));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete employee record")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
