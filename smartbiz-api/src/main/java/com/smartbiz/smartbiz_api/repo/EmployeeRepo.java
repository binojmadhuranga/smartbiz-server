package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);     //Future use for seracrh function...
    List<Employee> findByUserId(Long userId);
}
