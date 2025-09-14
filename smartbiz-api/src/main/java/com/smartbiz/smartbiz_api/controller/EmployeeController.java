package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.dto.EmployeeDto;
import com.smartbiz.smartbiz_api.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    private Long getUserId(HttpServletRequest request) {
        String userIdHeader = request.getAttribute("userId").toString();
        return Long.parseLong(userIdHeader);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto dto,
                                                   HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(employeeService.addEmployee(dto, userId));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(employeeService.getAllEmployeesByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id,
                                                       HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(employeeService.getEmployeeById(id, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id,
                                                      @RequestBody EmployeeDto dto,
                                                      HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id,
                                               HttpServletRequest request) {
        Long userId = getUserId(request);
        employeeService.deleteEmployee(id, userId);
        return ResponseEntity.noContent().build();
    }
}
