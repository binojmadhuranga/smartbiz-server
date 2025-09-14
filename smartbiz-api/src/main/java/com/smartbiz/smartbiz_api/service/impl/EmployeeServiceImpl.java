package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.EmployeeDto;
import com.smartbiz.smartbiz_api.entity.Employee;
import com.smartbiz.smartbiz_api.repo.EmployeeRepo;
import com.smartbiz.smartbiz_api.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepository;

    private EmployeeDto mapToDto(Employee employee) {
        return EmployeeDto.builder()
                .employeeId(employee.getEmployeeId())
                .name(employee.getName())
                .role(employee.getRole())
                .salary(employee.getSalary())
                .email(employee.getEmail())
                .build();
    }

    private Employee mapToEntity(EmployeeDto dto) {
        return Employee.builder()
                .employeeId(dto.getEmployeeId())
                .name(dto.getName())
                .role(dto.getRole())
                .salary(dto.getSalary())
                .email(dto.getEmail())
                .build();
    }

    @Override
    public EmployeeDto addEmployee(EmployeeDto dto) {
        Employee employee = employeeRepository.save(mapToEntity(dto));
        return mapToDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setName(dto.getName());
        employee.setRole(dto.getRole());
        employee.setSalary(dto.getSalary());
        employee.setEmail(dto.getEmail());
        return mapToDto(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }


}
