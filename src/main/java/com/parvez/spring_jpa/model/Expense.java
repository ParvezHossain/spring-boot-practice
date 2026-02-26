package com.parvez.spring_jpa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Monetary values should always use BigDecimal.
     * Never use double for money.
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * LocalDate is correct for business dates.
     */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * Many expenses belong to one category.
     * Category is mandatory → optional = false
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @NotBlank(message = "Description can not be empty")
    private String description;
}
