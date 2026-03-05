package com.kaek.pl.service;

import com.kaek.pl.domain.entity.Expenses;
import com.kaek.pl.exception.IdNotFound;
import com.kaek.pl.exception.InvalidInput;
import com.kaek.pl.repository.ExpensesRepository;
import com.kaek.pl.repository.ExpensesSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.time.LocalDate;

@Service
public class ExpensesService {

    private final ExpensesRepository expensesRepository;

    public ExpensesService(ExpensesRepository expensesRepository) {
        this.expensesRepository = expensesRepository;
    }

    public Expenses saveExpense(Expenses expense) {
        return expensesRepository.save(expense);
    }

    public Page<Expenses> getAllExpenses(String description, // filters
                                      String category,
                                      LocalDate dateAfter,
                                      LocalDate dateBefore,
                                      LocalDate date,
                                      Pageable pageable) {
        Specification<Expenses> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();
        // optional filters
        if (description != null) {
            spec = spec.and(ExpensesSpecification.hasDesc(description));
        }
        if (category != null) {
            spec = spec.and(ExpensesSpecification.hasCategory(category));
        }
        // date takes priority over date range
        if (date != null) {
            spec = spec.and(ExpensesSpecification.hasDate(date));
        } else if (dateAfter != null || dateBefore != null) {
            // invalid param check
            if (dateAfter == null || dateBefore == null) {
                throw new InvalidInput("Both limits of the date range must be provided.");
            }
            if (dateAfter.isAfter(dateBefore)) {
                throw new InvalidInput("The start date cannot be after the end date.");
            }
            spec = spec.and(ExpensesSpecification.hasDates(dateAfter, dateBefore));
        }
        return expensesRepository.findAll(spec, pageable);
    }

    public Expenses updateExpense(Long id, Expenses updatedExpense) {
        return expensesRepository.findById(id).map(expense -> {
            expense.setDescription(updatedExpense.getDescription());
            expense.setAmount(updatedExpense.getAmount());
            expense.setDate(updatedExpense.getDate());
            expense.setCategory(updatedExpense.getCategory());
            return expensesRepository.save(expense);
        }).orElseThrow(() -> new IdNotFound(id));
    }

    public void deleteExpense(Long id) {
        if (!expensesRepository.existsById(id)) {
            throw new IdNotFound(id);
        }
        expensesRepository.deleteById(id);
    }
}
