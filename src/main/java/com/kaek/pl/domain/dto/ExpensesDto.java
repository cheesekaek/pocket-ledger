package com.kaek.pl.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Data Transfer Object for Expenses entity")
public class ExpensesDto {

    @Schema(name = "description", example = "Lunch")
    @NotBlank(message = "The description must not be blank.")
    private String description;

    @Schema(name = "amount", example = "19.95")
    @NotNull(message = "The amount must not be null.")
    @PositiveOrZero(message = "The amount cannot be negative.")
    private Double amount;

    @Schema(name = "date", example = "2026-03-03")
    @NotNull(message = "The date must not be null.")
    private LocalDate date;

    @Schema(name = "category", example = "Food")
    @NotBlank(message = "The category must not be blank.")
    private String category;

}
