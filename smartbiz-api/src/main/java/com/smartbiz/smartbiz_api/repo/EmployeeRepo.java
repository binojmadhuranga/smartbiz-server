package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    List<Employee> findByUser_Id(Long userId);

    List<Employee> findByUser_IdAndNameContainingIgnoreCase(Long userId, String name);

    long countByUser_Id(Long userId);


}
