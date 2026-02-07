package com.parvez.spring_jpa.controller;

import com.parvez.spring_jpa.request_tracker.RequestCounterFilter;
import com.parvez.spring_jpa.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    @GetMapping("/request")
    public long request() {
        return RequestCounterFilter.getRequestCount();
    }

    @GetMapping("/employees/created")
    public long employeeCreated() {
        return EmployeeService.getEmployeeCreatedCount();
    }

    @GetMapping("employees/salary-updated")
    public long salaryUpdated() {
        return EmployeeService.getEmployeeSalaryUpdatedCount();
    }
}
