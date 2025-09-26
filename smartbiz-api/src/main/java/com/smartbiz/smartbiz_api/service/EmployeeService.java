package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.EmployeeDto;
import java.util.List;

public interface EmployeeService {
    EmployeeDto addEmployee(EmployeeDto employeeDto, Long userId);

    List<EmployeeDto> getAllEmployeesByUser(Long userId);

    EmployeeDto getEmployeeById(Long id, Long userId);

    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto, Long userId);

    void deleteEmployee(Long id, Long userId);

    List<EmployeeDto> searchEmployeesByName(String name, Long userId);


}
