package com.kaek.pl.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data // getters , setters
@Builder // for test
@AllArgsConstructor // constructors
@NoArgsConstructor
public class ExpensesDto {

    @NotBlank(message = "The description must not be blank.")
    private String description;

    @NotNull(message = "The amount must not be null.")
    @PositiveOrZero(message = "The amount cannot be negative.")
    private Double amount;

    @NotNull(message = "The date must not be null.")
    private LocalDate date;

    @NotBlank(message = "The category must not be blank.")
    private String category;

}
