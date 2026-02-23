package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.EmployeeDto;
import com.smartbiz.smartbiz_api.entity.Employee;
import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.exception.ForbiddenException;
import com.smartbiz.smartbiz_api.exception.NotFoundException;
import com.smartbiz.smartbiz_api.repo.EmployeeRepo;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepository;
    private final UserRepo userRepository;

    private EmployeeDto mapToDto(Employee employee) {
        return EmployeeDto.builder()
                .employeeId(employee.getEmployeeId())
                .name(employee.getName())
                .role(employee.getRole())
                .salary(employee.getSalary())
                .email(employee.getEmail())
                .userId(employee.getUser().getId())
                .build();
    }


    private Employee mapToEntity(EmployeeDto dto, User user) {

        return Employee.builder()
                .employeeId(dto.getEmployeeId())
                .name(dto.getName())
                .role(dto.getRole())
                .salary(dto.getSalary())
                .email(dto.getEmail())
                .user(user)
                .build();
    }

    @Override
    public EmployeeDto addEmployee(EmployeeDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Employee employee = mapToEntity(dto, user);
        Employee savedEmployee = employeeRepository.save(employee);

        return mapToDto(savedEmployee);
    }

    @Override
    public List<EmployeeDto> getAllEmployeesByUser(Long userId) {
        return employeeRepository.findByUser_Id(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto getEmployeeById(Long id, Long userId) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        if (!employee.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Unauthorized access to this employee");
        }

        return mapToDto(employee);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto dto, Long userId) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        if (!employee.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Unauthorized update attempt");
        }

        employee.setName(dto.getName());
        employee.setRole(dto.getRole());
        employee.setSalary(dto.getSalary());
        employee.setEmail(dto.getEmail());

        Employee updated = employeeRepository.save(employee);
        return mapToDto(updated);

    }

    @Override
    public List<EmployeeDto> searchEmployeesByName(String name, Long userId) {
        List<Employee> employees = employeeRepository.findByUser_IdAndNameContainingIgnoreCase(userId, name);
        return employees.stream()
                .map(this::mapToDto)
                .toList();

    }


    @Override
    public void deleteEmployee(Long id, Long userId) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        if (!employee.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Unauthorized delete attempt");
        }

        employeeRepository.delete(employee);
    }

}
