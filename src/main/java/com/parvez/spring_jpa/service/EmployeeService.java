package com.parvez.spring_jpa.service;

import com.parvez.spring_jpa.amqp.EmployeeSalaryEventPublisher;
import com.parvez.spring_jpa.dto.EmployeeRegisterDTO;
import com.parvez.spring_jpa.dto.EmployeeSalaryUpdatedEvent;
import com.parvez.spring_jpa.exceptions.ResourceNotFoundException;
import com.parvez.spring_jpa.dto.EmployeeResponseDTO;
import com.parvez.spring_jpa.model.Employee;
import com.parvez.spring_jpa.model.SalaryAudit;
import com.parvez.spring_jpa.repository.EmployeeRepository;
import com.parvez.spring_jpa.repository.SalaryAuditRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final SalaryAuditRepository salaryAuditRepository;
    private static final AtomicLong EMPLOYEE_CREATED_COUNT = new AtomicLong(0);
    private static final AtomicLong EMPLOYEE_SALARY_UPDATED_COUNT = new AtomicLong(0);
    private final EmployeeSalaryEventPublisher employeeSalaryEventPublisher;
    private final ExecutorService asyncExecutor = Executors.newFixedThreadPool(10);

    // ------------------------ CREATE EMPLOYEE ------------------------
    public EmployeeResponseDTO createEmployee(EmployeeRegisterDTO employeeCreateDTO) {

        validateCreateDto(employeeCreateDTO);

        Employee employee = new Employee();
        employee.setFirstName(employeeCreateDTO.firstName());
        employee.setLastName(employeeCreateDTO.lastName());
        employee.setUsername(employeeCreateDTO.username());
        employee.setEmail(employeeCreateDTO.email());
        employee.setAge(employeeCreateDTO.age());
        employee.setSalary(employeeCreateDTO.salary());

        employeeRepository.save(employee);
        EMPLOYEE_CREATED_COUNT.incrementAndGet();

        return mapToResponse(employee);
    }

    private void validateCreateDto(EmployeeRegisterDTO dto) {
        if (employeeRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Username already exists!");
        }
        if (employeeRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already exists!");
        }
    }

    // ------------------------ FIND AN EMPLOYEE ------------------------
    public Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    // ------------------------ FIND ALL EMPLOYEES ------------------------
    @Cacheable(
            value = "employees",
            key = "T(java.lang.String).format('%s-%s-%s-%s', #page, #size, #sortBy, #desc)"
    )
    public Page<EmployeeResponseDTO> findAllEmployees(
            int page,
            int size,
            String sortBy,
            boolean desc
    ) {
        Sort sort = desc
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<EmployeeResponseDTO> dtos = employeePage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new PageImpl<>(dtos, pageable, employeePage.getTotalElements());
    }

    // ------------------------ INCREASE SALARY ------------------------
    public EmployeeResponseDTO increaseSalary(Long id, Double amount) throws BadRequestException {

        validateSalaryIncrement(id, amount);

        System.out.println("testing increaseSalary");

        // @Lock(PESSIMISTIC_WRITE) | prevents two threads from updating the same row simultaneously
        Employee employee = employeeRepository.lockById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        Double currentSalary = Optional.ofNullable(employee.getSalary())
                .orElse(0.0);

        double newSalary = currentSalary + amount;
        employee.setSalary(newSalary);

        // --- SAVE SALARY AUDIT ---
        salaryAuditRepository.save(
                SalaryAudit.builder()
                        .employeeId(id)
                        .oldSalary(employee.getSalary() - amount)
                        .newSalary(employee.getSalary())
                        .incrementSalary(amount)
                        .changedAt(LocalDateTime.now())
                        .changedBy("SYSTEM_USER")
                        .build()
        );

        // Publish event asynchronously
        asyncExecutor.submit(() ->
                employeeSalaryEventPublisher.publish(
                        new EmployeeSalaryUpdatedEvent(
                                id,
                                employee.getSalary(),
                                newSalary,
                                amount,
                                LocalDateTime.now()
                        )
                ));

        EMPLOYEE_SALARY_UPDATED_COUNT.incrementAndGet();
        return mapToResponse(employee);
    }

    private void validateSalaryIncrement(Long id, Double amount) throws BadRequestException {
        if (id == null) throw new BadRequestException("id is null");
        if (amount == null || amount <= 0) throw new BadRequestException("Salary increment must be greater than zero");
    }

    // ------------------------ UTILITY ------------------------
    private EmployeeResponseDTO mapToResponse(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getUsername(),
                employee.getEmail(),
                employee.getAge(),
                employee.getSalary()
        );
    }

    public static long getEmployeeCreatedCount() {
        return EMPLOYEE_CREATED_COUNT.get();
    }

    public static long getEmployeeSalaryUpdatedCount() {
        return EMPLOYEE_SALARY_UPDATED_COUNT.get();
    }
}
