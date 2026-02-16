package com.example.pl.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity(name = "Expenses")
@Table(
        name = "expenses"
)
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

    @Column(
            name="id",
            updatable = false
    )
    private Long id;

    @Column(
            name="description",
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "amount",
            nullable = false
    )
    private Double amount;

    @Column(
            name = "date",
            nullable = false
    )
    private LocalDate date;

    @Column(
            name = "category",
            columnDefinition = "TEXT",
            nullable = false
    )
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
