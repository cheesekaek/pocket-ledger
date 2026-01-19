package com.example.pl;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity(name = "Expenses")
public class Expenses {

    @Id
    @SequenceGenerator(
            name = "expenses_seq",
            sequenceName = "expenses_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "expenses_seq"
    )
    private Long id;

    private String description;

    private Double amount;

    private LocalDate date;

    private String category;

    public Expenses() {
    }

    public Expenses(
            String description,
            Double amount,
            LocalDate date,
            String category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
