package com.parvez.spring_jpa.controller;

import com.parvez.spring_jpa.dto.EmployeeRegisterDTO;
import com.parvez.spring_jpa.dto.EmployeeResponseDTO;
import com.parvez.spring_jpa.model.Role;
import com.parvez.spring_jpa.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@EnableMethodSecurity
public class EmployeeController {
    private final EmployeeService employeeService;

    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "salary") String sortBy,
            @RequestParam(defaultValue = "false") boolean desc
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isHrOrAdmin = authentication
                .getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .anyMatch(role -> role.equals(Role.ADMIN.name())
                        || role.equals(Role.HR.name())
                );

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

//        if (!isHrOrAdmin) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }

        return ResponseEntity.ok(
                employeeService.findAllEmployees(page, size, sortBy, desc)
        );
    }


    /**
     * Salary increment endpoint:
     * - Only accessible by HR
     * - Uses JWT from SecurityContext
     */

    @PreAuthorize("hasRole('HR')")
    @PostMapping("/{id}/salary/increment")
    public ResponseEntity<EmployeeResponseDTO> incrementSalary(
            @PathVariable Long id,
            @RequestParam @Positive(message = "Increment amount must be positive") Double amount
    ) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

/*
        boolean isHr = authentication
                .getAuthorities()
                .stream().
                anyMatch(a -> a.getAuthority().equals(STR."ROLE_\{Role.HR}"));//        if (!isHr) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
*/


        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        EmployeeResponseDTO responseDTO = employeeService.increaseSalary(id, amount);
        return ResponseEntity.ok(responseDTO);
    }

}
