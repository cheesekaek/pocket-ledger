package com.example.pl.service;

import com.example.pl.entity.Expenses;
import com.example.pl.repository.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpensesRepository expensesRepository;

    public Expenses saveExpense(Expenses expense) {
        return expensesRepository.save(expense);
    }

    public List<Expenses> getAllExpenses() {
        return expensesRepository.findAll();
    }

    public Expenses updateExpense(Long id, Expenses updatedExpense) {
        return expensesRepository.findById(id).map(expense -> {
            expense.setDescription(updatedExpense.getDescription());
            expense.setAmount(updatedExpense.getAmount());
            expense.setDate(updatedExpense.getDate());
            expense.setCategory(updatedExpense.getCategory());
            return expensesRepository.save(expense);
        }).orElseThrow(() -> new RuntimeException("The id " + id + " does not exist."));
    }

    public void deleteExpense(Long id) {
        if (!expensesRepository.existsById(id)) {
            throw new RuntimeException("The id " + id + " does not exist.");
        }
        expensesRepository.deleteById(id);
    }
}
