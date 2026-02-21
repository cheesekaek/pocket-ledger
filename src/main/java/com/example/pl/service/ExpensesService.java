package com.example.pl.service;

import com.example.pl.entity.Expenses;
import com.example.pl.exception.IdNotFound;
import com.example.pl.repository.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Expenses> getExpensesByCategory(String category) { return expensesRepository.findByCategory(category); }

    public List<Expenses> getExpensesByDescription(String description) { return expensesRepository.findByDescription(description); }

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
