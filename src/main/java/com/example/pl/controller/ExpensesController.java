package com.example.pl.controller;

import com.example.pl.dto.ExpensesDto;
import com.example.pl.mapper.ExpensesMapper;
import com.example.pl.entity.Expenses;
import com.example.pl.service.ExpensesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpensesController {

    @Autowired
    private ExpensesService expensesService;

    @Autowired
    private ExpensesMapper expensesMapper;

    @PostMapping
    public ResponseEntity<ExpensesDto> createExpense(@Valid @RequestBody ExpensesDto dto) {
        // convert to entity
        Expenses expense = expensesMapper.toExpEntity(dto);
        // save entity in service
        Expenses saved = expensesService.saveExpense(expense);
        // convert back to dto
        return ResponseEntity.ok(expensesMapper.toExpDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<ExpensesDto>> getAllExpenses() {
        // list of entities
        List<Expenses> expensesList = expensesService.getAllExpenses();
        // list of dtos
        List<ExpensesDto> expensesDtoList = expensesList.stream().map(expensesMapper::toExpDto).toList();
        // return the latter
        return ResponseEntity.ok(expensesDtoList);
    }

    @GetMapping
    public ResponseEntity<List<ExpensesDto>> getExpensesByDescription(String description) {
        List<Expenses> expensesList = expensesService.getExpensesByDescription(description);
        List<ExpensesDto> expensesDtoList = expensesList.stream().map(expensesMapper::toExpDto).toList();
        return ResponseEntity.ok(expensesDtoList);
    }

    @GetMapping
    public ResponseEntity<List<ExpensesDto>> getExpensesByCategory(String category) {
        List<Expenses> expensesList = expensesService.getExpensesByCategory(category);
        List<ExpensesDto> expensesDtoList = expensesList.stream().map(expensesMapper::toExpDto).toList();
        return ResponseEntity.ok(expensesDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpensesDto> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpensesDto dto) {
        Expenses expense = expensesMapper.toExpEntity(dto);
        Expenses updated = expensesService.updateExpense(id, expense);
        return ResponseEntity.ok(expensesMapper.toExpDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expensesService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

}
