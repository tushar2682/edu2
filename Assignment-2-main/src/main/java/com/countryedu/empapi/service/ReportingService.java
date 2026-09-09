package com.countryedu.empapi.service;

import com.countryedu.empapi.dto.DepartmentHeadcountDTO;
import com.countryedu.empapi.dto.EmployeeResponseDTO;
import com.countryedu.empapi.dto.ProjectUtilizationDTO;

import java.util.List;

public interface ReportingService {
    List<DepartmentHeadcountDTO> getDepartmentHeadcounts();
    List<ProjectUtilizationDTO> getProjectUtilizationSummary();
    List<EmployeeResponseDTO> getUnderutilizedEmployees();
}
