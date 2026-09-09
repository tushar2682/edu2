package com.countryedu.empapi.repository;

import com.countryedu.empapi.dto.DepartmentHeadcountDTO;
import com.countryedu.empapi.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    @Override
    @EntityGraph(attributePaths = {"department"})
    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"department"})
    Optional<Employee> findById(Long id);

    @Query("SELECT e FROM Employee e " +
           "LEFT JOIN FETCH e.department d " +
           "LEFT JOIN FETCH e.assignments a " +
           "LEFT JOIN FETCH a.project p " +
           "WHERE e.id = :id")
    Optional<Employee> findByIdWithDetails(@Param("id") Long id);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmail(String email);

    @Query("SELECT new com.countryedu.empapi.dto.DepartmentHeadcountDTO(" +
           "d.id, d.name, d.code, COUNT(e.id), AVG(e.salary), SUM(e.salary)) " +
           "FROM Department d " +
           "LEFT JOIN d.employees e " +
           "GROUP BY d.id, d.name, d.code")
    List<DepartmentHeadcountDTO> getDepartmentHeadcounts();
}
