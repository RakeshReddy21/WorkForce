package org.example.workforce.service;
import org.example.workforce.dto.RegisterEmployeeRequest;
import org.example.workforce.model.*;
import org.example.workforce.model.enums.*;
import org.example.workforce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    public Employee registerEmployee(RegisterEmployeeRequest request){
        if(employeeRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists: "+ request.getEmail());
        }
        if(employeeRepository.existsByEmployeeCode(request.getEmployeeCode())){
            throw new RuntimeException("Employee code already exists: "+ request.getEmployeeCode());
        }
        Employee employee = Employee.builder().firstName(request.getFirstName()).lastName(request.getLastName()).email(request.getEmail()).passwordHash(passwordEncoder.encode(request.getPassword())).employeeCode(request.getEmployeeCode()).phone(request.getPhone()).dateOfBirth(request.getDateOfBirth()).address(request.getAddress()).emergencyContactName(request.getEmergencyContactName()).emergencyContactPhone(request.getEmergencyContactPhone()).joiningDate(request.getJoiningDate()).salary(request.getSalary()).build();
        if(request.getGender() != null && !request.getGender().isBlank()){
            employee.setGender(Gender.valueOf(request.getGender().toUpperCase()));
        }
        if(request.getRole() != null && !request.getRole().isBlank()){
            employee.setRole(Role.valueOf(request.getRole().toUpperCase()));
        }
        if(request.getDepartmentId() != null){
            Department dept = departmentRepository.findById(request.getDepartmentId()).orElseThrow(()-> new RuntimeException("Department not found: " + request.getDepartmentId()));
            employee.setDepartment(dept);
        }
        if(request.getDesignationId() != null){
            Designation desig = designationRepository.findById(request.getDesignationId()).orElseThrow(()-> new RuntimeException("Designation not found: " + request.getDesignationId()));
            employee.setDesignation(desig);
        }
        if(request.getManagerId() != null){
            Employee manager = employeeRepository.findById(request.getManagerId()).orElseThrow(()-> new RuntimeException("Manager not found: " + request.getManagerId()));
            employee.setManager(manager);
        }
        return employeeRepository.save(employee);
    }
}