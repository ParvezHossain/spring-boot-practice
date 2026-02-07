package com.parvez.spring_jpa.Initializer;

import com.parvez.spring_jpa.dto.EmployeeRegisterDTO;
import com.parvez.spring_jpa.model.Role;
import com.parvez.spring_jpa.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EmployeeService employeeService;

    @Override
    public void run(String... args) throws Exception {
        EmployeeRegisterDTO dto = new EmployeeRegisterDTO(
                "Parvez",
                "Hossain",
                "P@123arv",
                "parvez@gmail.com",
                31,
                100000.0,
                "1234",
                Role.EMPLOYEE
        );
//        EmployeeResponseDTO e = employeeService.createEmployee(dto);
//        System.out.println(STR."Employee inserted successfully: \{e}");

    }
}
