package com.kaek.pl.controller;

import com.kaek.pl.domain.dto.ExpensesDto;
import com.kaek.pl.domain.mapper.ExpensesMapper;
import com.kaek.pl.domain.entity.Expenses;
import com.kaek.pl.service.ExpensesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "Expenses API")
public class ExpensesController {

    private final ExpensesService expensesService;

    private final ExpensesMapper expensesMapper;

    public ExpensesController(ExpensesService expensesService, ExpensesMapper expensesMapper) {
        this.expensesService = expensesService;
        this.expensesMapper = expensesMapper;
    }

    @Operation(summary = "Creates a new expense",
            description = "Requires a valid request body (description, category, amount, and date)." +
                    " Returns the created expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    public ResponseEntity<ExpensesDto> createExpense(@Valid @RequestBody ExpensesDto dto) {
        // convert to entity
        Expenses expense = expensesMapper.toExpEntity(dto);
        // save entity in service
        Expenses saved = expensesService.saveExpense(expense);
        // convert back to dto
        return ResponseEntity.status(HttpStatus.CREATED).body(expensesMapper.toExpDto(saved));
    }

    @Operation(summary = "Retrieve expenses with optional filters",
            description = "Returns a paginated list of expenses" +
                    "All filters are optional and can be combined." +
                    "Dates must be in ISO format (yyyy-MM-dd)." +
                    "When using date range, dateAfter must be before or equal to dateBefore." +
                    "If dateAfter and/or dateBefore are used with date, only date is considered as a filter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date format, " +
                    "only one date range limit provided, or dateAfter is after dateBefore")
    })
    @Parameter(name = "description", description = "Filter expenses by description", example = "Lunch")
    @Parameter(name = "category", description = "Filter expenses by category", example = "Food")
    @Parameter(name = "dateAfter", description = "Filter expenses after this date (yyyy-MM-dd)", example = "2026-03-03")
    @Parameter(name = "dateBefore", description = "Filter expenses before this date (yyyy-MM-dd)", example = "2026-04-04")
    @Parameter(name = "date", description = "Filter expenses by date (yyyy-MM-dd)", example = "2026-03-03")
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

    @Operation(summary = "Update expense by ID",
            description = "Updates an existing expense by ID. Requires a valid request body " +
                    "(description, category, amount, and date). Returns the updated expense.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Expense with ID cannot be found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExpensesDto> updateExpense(
            @Parameter(name = "id", description = "ID of the expense to be updated", example = "6")
            @PathVariable Long id,
            @Valid @RequestBody ExpensesDto dto) {
        Expenses expense = expensesMapper.toExpEntity(dto);
        Expenses updated = expensesService.updateExpense(id, expense);
        return ResponseEntity.ok(expensesMapper.toExpDto(updated));
    }

    @Operation(summary = "Delete expense by ID", description = "Deletes an existing expense by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Expense with ID cannot be found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @Parameter(name = "id", description = "ID of the expense to be deleted", example = "7")
            @PathVariable Long id) {
        expensesService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

}
