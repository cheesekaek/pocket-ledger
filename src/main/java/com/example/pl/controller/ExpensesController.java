package com.example.pl.controller;

import com.example.pl.dto.ExpensesDto;
import com.example.pl.mapper.ExpensesMapper;
import com.example.pl.entity.Expenses;
import com.example.pl.service.ExpensesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

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
    public ResponseEntity<Page<ExpensesDto>> getAllExpenses(@RequestParam (required = false)
                                                                String description, // filter by desc
                                                            @RequestParam (required = false)
                                                                String category, // filter by category
                                                            @RequestParam (required = false)
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // conversion from string to date
                                                                LocalDate dateAfter, // filter by b/w dates
                                                            @RequestParam (required = false)
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                LocalDate dateBefore,
                                                            @RequestParam (required = false)
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                LocalDate date, // filter by date
                                                            Pageable pageable) {
        // entities
        Page<Expenses> expensesPage = expensesService.getAllExpenses(description, category, dateAfter, dateBefore, date, pageable);
        // to dto
        Page<ExpensesDto> expensesDtoPage = expensesPage.map(expensesMapper::toExpDto);
        // return dto page
        return ResponseEntity.ok(expensesDtoPage);
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
