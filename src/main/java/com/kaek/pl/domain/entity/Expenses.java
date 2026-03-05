package com.kaek.pl.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder // for test
@Data  // getters , setters
@AllArgsConstructor // for test
@Entity(name = "Expenses")
@Table(name = "expenses")
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
            columnDefinition = "TEXT",
            nullable = false
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

    public Expenses() {}

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
}
