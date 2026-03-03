package com.example.pocketledger.service;

import com.example.pl.entity.Expenses;
import com.example.pl.repository.ExpensesRepository;
import com.example.pl.service.ExpensesService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

/**
 * general format :
 * Arrange
 * Act
 * Assert
 * Verify
 */
@ExtendWith(MockitoExtension.class)
public class ExpensesServiceTest {

    // mock repository to avoid touching db
    @Mock
    private ExpensesRepository expensesRepository;

    // bring service
    @InjectMocks
    private ExpensesService expensesService;

    private final Pageable pageable = PageRequest.of(0, 10);

    @Test
    public void ExpensesService_SaveExpense() {
        // arrange
        Expenses expense = Expenses.builder()
                .description("Lunch")
                .category("Food")
                .amount(22.50)
                .date(LocalDate.of(2026, 3, 3))
                .build();

        when(expensesRepository.save(any(Expenses.class))).thenReturn(expense);

        // act
        Expenses savedExpense = expensesService.saveExpense(expense);

        // assert
        Assertions.assertThat(expense).isNotNull();
        Assertions.assertThat(savedExpense.getDescription()).isEqualTo("Lunch");
        Assertions.assertThat(savedExpense.getCategory()).isEqualTo("Food");
        Assertions.assertThat(savedExpense.getAmount()).isEqualTo(22.50);
        Assertions.assertThat(savedExpense.getDate()).isEqualTo(LocalDate.of(2026, 3, 3));

        // verify
        verify(expensesRepository, times(1)).save(any(Expenses.class));
    }

    @Test
    public void ExpensesService_NoFilters_getAllExpenses() {
        Expenses expense1 = Expenses.builder()
                .description("Lunch")
                .category("Food")
                .amount(22.50)
                .date(LocalDate.of(2026, 3, 3))
                .build();
        Expenses expense2 = Expenses.builder()
                .description("Bus Fare")
                .category("Transit")
                .amount(3.00)
                .date(LocalDate.of(2026, 4, 4))
                .build();
        Page<Expenses> page = new PageImpl<>(List.of(expense1, expense2));

        when(expensesRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<Expenses> result = expensesService.getAllExpenses(
                null, null, null, null, null, pageable
        );

        Assertions.assertThat(result.getContent()).hasSize(2);
        Assertions.assertThat(result.getContent().get(0).getDescription()).isEqualTo("Lunch");
        Assertions.assertThat(result.getContent().get(0).getCategory()).isEqualTo("Food");
        Assertions.assertThat(result.getContent().get(0).getAmount()).isEqualTo(22.50);
        Assertions.assertThat(result.getContent().get(0).getDate()).isEqualTo(LocalDate.of(2026, 3, 3));

        Assertions.assertThat(result.getContent().get(1).getDescription()).isEqualTo("Bus Fare");
        Assertions.assertThat(result.getContent().get(1).getCategory()).isEqualTo("Transit");
        Assertions.assertThat(result.getContent().get(1).getAmount()).isEqualTo(3.00);
        Assertions.assertThat(result.getContent().get(1).getDate()).isEqualTo(LocalDate.of(2026, 4, 4));

        verify(expensesRepository).findAll(any(Specification.class), eq(pageable));
    }
}
