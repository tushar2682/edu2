package com.countryedu.empapi.config;

import com.countryedu.empapi.entity.*;
import com.countryedu.empapi.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserAccountRepository userAccountRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectAssignmentRepository assignmentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserAccountRepository userAccountRepository,
                           DepartmentRepository departmentRepository,
                           EmployeeRepository employeeRepository,
                           ProjectRepository projectRepository,
                           ProjectAssignmentRepository assignmentRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userAccountRepository = userAccountRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.assignmentRepository = assignmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() > 0) {
            return;
        }

        log.info("Bootstrapping initial demo data for Scalable Employee Management API...");

        // 1. Roles
        Role adminRole = roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
        Role managerRole = roleRepository.save(Role.builder().name("ROLE_MANAGER").build());
        Role employeeRole = roleRepository.save(Role.builder().name("ROLE_EMPLOYEE").build());

        // 2. User Accounts
        userAccountRepository.save(UserAccount.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles(Set.of(adminRole, managerRole, employeeRole))
                .build());

        userAccountRepository.save(UserAccount.builder()
                .username("manager")
                .password(passwordEncoder.encode("manager123"))
                .roles(Set.of(managerRole, employeeRole))
                .build());

        userAccountRepository.save(UserAccount.builder()
                .username("employee")
                .password(passwordEncoder.encode("employee123"))
                .roles(Set.of(employeeRole))
                .build());

        // 3. Departments
        Department engineering = departmentRepository.save(Department.builder()
                .name("Engineering")
                .code("ENG")
                .build());

        Department product = departmentRepository.save(Department.builder()
                .name("Product Management")
                .code("PROD")
                .build());

        Department hr = departmentRepository.save(Department.builder()
                .name("Human Resources")
                .code("HR")
                .build());

        // 4. Employees
        Employee emp1 = employeeRepository.save(Employee.builder()
                .employeeCode("EMP001")
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice.johnson@countryedu.com")
                .phone("+1-555-0101")
                .jobTitle("Senior Software Engineer")
                .salary(new BigDecimal("95000.00"))
                .hireDate(LocalDate.of(2022, 3, 15))
                .status(EmployeeStatus.ACTIVE)
                .department(engineering)
                .build());

        Employee emp2 = employeeRepository.save(Employee.builder()
                .employeeCode("EMP002")
                .firstName("Bob")
                .lastName("Smith")
                .email("bob.smith@countryedu.com")
                .phone("+1-555-0102")
                .jobTitle("Principal Architect")
                .salary(new BigDecimal("145000.00"))
                .hireDate(LocalDate.of(2021, 1, 10))
                .status(EmployeeStatus.ACTIVE)
                .department(engineering)
                .build());

        Employee emp3 = employeeRepository.save(Employee.builder()
                .employeeCode("EMP003")
                .firstName("Carol")
                .lastName("White")
                .email("carol.white@countryedu.com")
                .phone("+1-555-0103")
                .jobTitle("HR Specialist")
                .salary(new BigDecimal("78000.00"))
                .hireDate(LocalDate.of(2023, 6, 1))
                .status(EmployeeStatus.ACTIVE)
                .department(hr)
                .build());

        Employee emp4 = employeeRepository.save(Employee.builder()
                .employeeCode("EMP004")
                .firstName("David")
                .lastName("Brown")
                .email("david.brown@countryedu.com")
                .phone("+1-555-0104")
                .jobTitle("Product Director")
                .salary(new BigDecimal("125000.00"))
                .hireDate(LocalDate.of(2021, 8, 20))
                .status(EmployeeStatus.ACTIVE)
                .department(product)
                .build());

        // 5. Projects
        Project proj1 = projectRepository.save(Project.builder()
                .projectCode("PRJ-001")
                .name("Cloud Modernization Platform")
                .description("Migrating monolithic workflows to microservices on AWS")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(ProjectStatus.ACTIVE)
                .budget(new BigDecimal("350000.00"))
                .build());

        Project proj2 = projectRepository.save(Project.builder()
                .projectCode("PRJ-002")
                .name("NextGen Mobile Portal")
                .description("React Native cross-platform mobile experience")
                .startDate(LocalDate.of(2024, 4, 1))
                .endDate(LocalDate.of(2025, 3, 31))
                .status(ProjectStatus.ACTIVE)
                .budget(new BigDecimal("200000.00"))
                .build());

        // 6. Assignments
        assignmentRepository.save(ProjectAssignment.builder()
                .employee(emp1)
                .project(proj1)
                .roleInProject("Backend Lead")
                .allocationPercentage(60)
                .assignedDate(LocalDate.of(2024, 1, 15))
                .status(AssignmentStatus.ACTIVE)
                .build());

        assignmentRepository.save(ProjectAssignment.builder()
                .employee(emp2)
                .project(proj1)
                .roleInProject("System Architect")
                .allocationPercentage(50)
                .assignedDate(LocalDate.of(2024, 1, 15))
                .status(AssignmentStatus.ACTIVE)
                .build());

        log.info("Demo data bootstrap complete: 3 departments, 4 employees, 2 projects, 2 assignments, 3 users created.");
    }
}
