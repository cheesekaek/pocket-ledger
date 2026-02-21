package com.example.pl.service;

import com.example.pl.entity.Expenses;
import com.example.pl.exception.IdNotFound;
import com.example.pl.exception.InvalidInput;
import com.example.pl.repository.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpensesService {

    @Autowired
    private ExpensesRepository expensesRepository;

    public Expenses saveExpense(Expenses expense) {
        return expensesRepository.save(expense);
    }

    public List<Expenses> getAllExpenses() {
        return expensesRepository.findAll();
    }

    public List<Expenses> getExpensesByCategory(String category) {
        if (category == null) {
            throw new InvalidInput("The category must be provided.");
        }
        return expensesRepository.findByCategory(category);
    }

    public List<Expenses> getExpensesByDescription(String description) {
        if (description == null) {
            throw new InvalidInput("The description must be provided.");
        }
        return expensesRepository.findByDescription(description);
    }

    public List<Expenses> getExpensesByDate(LocalDate date) {
        if (date == null) {
            throw new InvalidInput("The date must be provided.");
        }
        return expensesRepository.findByDate(date);
    }

    public List<Expenses> getExpensesByDateBetween(LocalDate dateAfter, LocalDate dateBefore) {
        if (dateAfter == null || dateBefore == null) {
            throw new InvalidInput("Both limits of the date range must be provided.");
        }
        if (dateAfter.isAfter(dateBefore)) {
            throw new InvalidInput("The start date cannot be after the end date.");
        }
        return expensesRepository.findByDateBetween(dateAfter, dateBefore);
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
