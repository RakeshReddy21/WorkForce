package org.example.workforce.controller;
import jakarta.validation.Valid;
import org.example.workforce.dto.ApiResponse;
import org.example.workforce.dto.RegisterEmployeeRequest;
import org.example.workforce.model.Employee;
import org.example.workforce.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/employees")
public class AdminEmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerEmployee(@Valid @RequestBody RegisterEmployeeRequest request){
        try{
            Employee employee = employeeService.registerEmployee(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Employee registered successfully", employee));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
