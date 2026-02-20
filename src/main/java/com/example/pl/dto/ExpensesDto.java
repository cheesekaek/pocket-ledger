package com.example.pl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
