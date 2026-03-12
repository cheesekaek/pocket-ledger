package com.kaek.pl.controller.docs;

import com.kaek.pl.domain.dto.ExpensesDto;
import com.kaek.pl.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@SuppressWarnings("unused")
public interface ExpensesControllerDocs {


    // =========================== POST ===========================
    @Operation(summary = "Creates a new expense",
            description = "Requires a valid request body (description, amount, date, category)." +
                    " Returns the created expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": 400, \"message\": \"description: " +
                                            "The description must not be blank. amount: The amount must not be null.\"}"
                            )))
    })
    ResponseEntity<ExpensesDto> createExpense(@Valid @RequestBody ExpensesDto dto);


    // =========================== GET ===========================
    @Operation(summary = "Retrieve expenses with optional filters",
            description = "Returns a paginated list of expenses. All filters are optional and can be combined. " +
                    "Dates must be in ISO format (yyyy-MM-dd). When using date range, dateAfter must be before or equal to dateBefore. " +
                    "If dateAfter and/or dateBefore are used with date, only date is considered as a filter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class),
                            examples = @ExampleObject(
                                    value = "{\"content\": [{\"description\": \"Lunch\", \"amount\": 19.95, \"date\": " +
                                            "\"2026-03-03\", \"category\": \"Food\"}], \"totalPages\": 1, \"totalElements\": 1, " +
                                            "\"size\": 10, \"number\": 0, \"first\": true, \"last\": true}"
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid date format, " +
                    "only one date range limit provided, or dateAfter is after dateBefore",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": 400, \"message\": \"The start date cannot be after the end date.\"}"
                            )))
    })
    @Parameter(name = "description", description = "Filter expenses by description", example = "Lunch")
    @Parameter(name = "category", description = "Filter expenses by category", example = "Food")
    @Parameter(name = "dateAfter", description = "Filter expenses after this date (yyyy-MM-dd)", example = "2026-03-03")
    @Parameter(name = "dateBefore", description = "Filter expenses before this date (yyyy-MM-dd)", example = "2026-04-04")
    @Parameter(name = "date", description = "Filter expenses by date (yyyy-MM-dd)", example = "2026-03-03")
    ResponseEntity<Page<ExpensesDto>> getAllExpenses(@RequestParam(required = false)
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
                                                            @Parameter(hidden = true)
                                                            Pageable pageable);


    // =========================== PUT ===========================
    @Operation(summary = "Update expense by ID",
            description = "Updates an existing expense by ID. Requires a valid request body " +
                    "(description, amount, date, category). Returns the updated expense.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Expense updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": 400, \"message\": \"description: " +
                                            "The description must not be blank. amount: The amount must not be null.\"}"
                            ))),
            @ApiResponse(responseCode = "404",
                    description = "Expense with ID cannot be found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"The id 6 cannot be found\"}"
                            )))
    })
    ResponseEntity<ExpensesDto> updateExpense(
            @Parameter(name = "id", description = "ID of the expense to be updated", example = "6")
            @PathVariable Long id,
            @Valid @RequestBody ExpensesDto dto);


    // =========================== DELETE ===========================
    @Operation(summary = "Delete expense by ID", description = "Deletes an existing expense by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense deleted successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Expense with the given ID not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"The id 7 cannot be found\"}"
                            )))
    })
    ResponseEntity<Void> deleteExpense(
            @Parameter(name = "id", description = "ID of the expense to be deleted", example = "7")
            @PathVariable Long id);
}
