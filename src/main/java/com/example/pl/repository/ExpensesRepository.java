package com.example.pl.repository;

import com.example.pl.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long> {
}
