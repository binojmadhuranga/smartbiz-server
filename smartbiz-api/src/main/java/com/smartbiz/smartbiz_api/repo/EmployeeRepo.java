package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    List<Employee> findByUserId(Long userId);
}
