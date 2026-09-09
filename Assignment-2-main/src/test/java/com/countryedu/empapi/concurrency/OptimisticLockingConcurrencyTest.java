package com.countryedu.empapi.concurrency;

import com.countryedu.empapi.dto.EmployeeResponseDTO;
import com.countryedu.empapi.entity.Department;
import com.countryedu.empapi.entity.Employee;
import com.countryedu.empapi.entity.EmployeeStatus;
import com.countryedu.empapi.repository.DepartmentRepository;
import com.countryedu.empapi.repository.EmployeeRepository;
import com.countryedu.empapi.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OptimisticLockingConcurrencyTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    @DisplayName("Should detect concurrent modification and throw ObjectOptimisticLockingFailureException")
    void testConcurrentSalaryUpdates_TriggerOptimisticLock() throws InterruptedException {
        Department dept = departmentRepository.save(Department.builder()
                .name("Finance")
                .code("FIN")
                .build());

        Employee employee = employeeRepository.save(Employee.builder()
                .employeeCode("CONCUR_001")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .jobTitle("Analyst")
                .salary(BigDecimal.valueOf(50000))
                .hireDate(LocalDate.now())
                .status(EmployeeStatus.ACTIVE)
                .department(dept)
                .build());

        Long empId = employee.getId();
        Long initialVersion = employee.getVersion();

        int numberOfThreads = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger conflictCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            final BigDecimal newSalary = BigDecimal.valueOf(60000 + (i * 5000));
            executor.submit(() -> {
                try {
                    startLatch.await(); // Synchronize all threads to fire at the exact same moment
                    employeeService.updateSalary(empId, newSalary, initialVersion);
                    successCount.incrementAndGet();
                } catch (ObjectOptimisticLockingFailureException ex) {
                    conflictCount.incrementAndGet();
                } catch (Exception e) {
                    // unexpected error
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // Start race condition
        boolean completed = endLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(completed).isTrue();
        // Exactly one thread succeeds, and the other encounters optimistic lock collision
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(conflictCount.get()).isEqualTo(1);

        // Verify version incremented in DB
        Employee updatedEmployee = employeeRepository.findById(empId).orElseThrow();
        assertThat(updatedEmployee.getVersion()).isEqualTo(initialVersion + 1);
    }
}
