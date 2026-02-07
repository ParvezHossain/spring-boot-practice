package com.parvez.spring_jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
//import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Check;

@Entity
@Check(constraints = "age >= 0")
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employee_username",
                        columnNames = "username"
                ),
                @UniqueConstraint(
                        name = "uk_emmployee_email",
                        columnNames = "email"
                )
        }
)
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version = 0L; // Optimistic locking

    /**
     * Username rules:
     * - exactly 8 characters
     * - at least one uppercase
     * - at least one lowercase
     * - at least one special character
     */

    @NotBlank(message = "Username is required")
    @Size(min = 8, max = 8, message = "Username must be exactly 8 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8}$",
            message = "Username must contain upper, lower, and special character"
    )
    @Column(unique = true, nullable = false, length = 8)
    private String username;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email  format")
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Age:
     * - Bean validation
     * - SQL-level constraint
     */
    @Min(value = 0, message = "Age can not be negative")
    @Column(name = "age", nullable = false)
    private Integer age;

    @PositiveOrZero(message = "Salary can not be negative")
    @Column(nullable = false)
    private Double salary;

    /**
     * Password:
     * - Stored as BCrypt hash
     * - Never serialized to JSON
     */

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private Role role = Role.EMPLOYEE; //default role


//    public Employee(String username, String firstName, String lastName, String email, Integer age, Double salary) {
//        this.username = username;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.age = age;
//        this.salary = salary;
//    }

    public Employee() {
    }
}
