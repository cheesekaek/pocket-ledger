package com.example.pocketledger.service;

import com.example.pl.entity.Expenses;
import com.example.pl.exception.IdNotFound;
import com.example.pl.exception.InvalidInput;
import com.example.pl.repository.ExpensesRepository;
import com.example.pl.service.ExpensesService;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    private Expenses sampleExpense;

    // sample entity
    @BeforeEach
    public void setSampleExpense() {
        sampleExpense = Expenses.builder()
                .description("Lunch")
                .category("Food")
                .amount(22.50)
                .date(LocalDate.of(2026, 3, 3))
                .build();
    }

    private final Pageable pageable = PageRequest.of(0, 10);

    // ======================= save expense =======================
    @Test
    public void ExpensesService_SaveExpense() {
        // arrange - sampleExpense
        when(expensesRepository.save(any(Expenses.class))).thenReturn(sampleExpense);

        // act
        Expenses savedExpense = expensesService.saveExpense(sampleExpense);

        // assert
        assertThat(savedExpense).isNotNull();
        assertThat(savedExpense.getDescription()).isEqualTo("Lunch");
        assertThat(savedExpense.getCategory()).isEqualTo("Food");
        assertThat(savedExpense.getAmount()).isEqualTo(22.50);
        assertThat(savedExpense.getDate()).isEqualTo(LocalDate.of(2026, 3, 3));
    }

    // ======================= get expenses =======================
    // no filters
    @Test
    public void ExpensesService_NoFilters_getAllExpenses() {
        Page<Expenses> page = new PageImpl<>(List.of(sampleExpense));
        when(expensesRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Expenses> result = expensesService.getAllExpenses(
                null, null, null, null, null, pageable
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDescription()).isEqualTo("Lunch");
    }

    // desc filter
    @Test
    public void ExpensesService_DescFilter_getAllExpensesWithDesc() {
        Page<Expenses> page = new PageImpl<>(List.of(sampleExpense));
        when(expensesRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Expenses> result = expensesService.getAllExpenses(
                "Lunch", null, null, null, null, pageable
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDescription()).isEqualTo("Lunch");
    }

    // valid date range
    @Test
    public void ExpensesService_ValidDateRangeFilter_getAllExpensesWithinRange() {
        Page<Expenses> page = new PageImpl<>(List.of(sampleExpense));
        when(expensesRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Expenses> result = expensesService.getAllExpenses(
                null, null, LocalDate.of(2026,2,2),
                LocalDate.of(2026,4,4), null, pageable
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDate()).isBetween(
                LocalDate.of(2026,2,2),LocalDate.of(2026,4,4));
    }

    // invalid date range
    @Test
    public void ExpensesService_OnlyDateAfter_ThrowInvalid() {
        assertThatThrownBy(() -> expensesService.getAllExpenses(
                null, null, LocalDate.of(2026, 3, 1),
                null, null, pageable))
                .isInstanceOf(InvalidInput.class);

        verify(expensesRepository, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    public void ExpensesService_OnlyDateBefore_ThrowInvalid() {
        assertThatThrownBy(() -> expensesService.getAllExpenses(
                null, null, null,
                LocalDate.of(2026, 4, 4), null, pageable))
                .isInstanceOf(InvalidInput.class);

        verify(expensesRepository, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    public void ExpensesService_InvertedDateRange_ThrowInvalid() {
        assertThatThrownBy(() -> expensesService.getAllExpenses(
                null, null, LocalDate.of(2026, 4, 4),
                LocalDate.of(2026, 2, 2), null, pageable))
                .isInstanceOf(InvalidInput.class)
                .hasMessageContaining("The start date cannot be after the end date.");

        verify(expensesRepository, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    // date priority over date range
    @Test
    public void ExpensesService_BothDateAndDateRange_getAllExpensesWithDate() {
        Page<Expenses> page = new PageImpl<>(List.of(sampleExpense));
        when(expensesRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Expenses> result = expensesService.getAllExpenses(
                null, null, LocalDate.of(2026,4,4),
                LocalDate.of(2026,5,5), LocalDate.of(2026,3,3), pageable
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDate()).isEqualTo(LocalDate.of(2026,3,3));
    }

    // ======================= update expense =======================
    @Test
    public void ExpensesService_WithValidId_UpdateExpense() {
        Expenses updated = Expenses.builder()
                .description("BreakFast")
                .category("Food")
                .amount(17.75)
                .date(LocalDate.of(2026,5,1))
                .build();
        when(expensesRepository.findById(1L)).thenReturn(Optional.of(sampleExpense));
        when(expensesRepository.save(any(Expenses.class))).thenReturn(sampleExpense);

        Expenses result = expensesService.updateExpense(1L, updated);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("BreakFast");
        assertThat(result.getAmount()).isEqualTo(17.75);
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2026,5,1));
    }

    @Test
    public void ExpensesService_UpdateWithInvalidId_ThrowIdNotFound() {
        when(expensesRepository.findById(11L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expensesService.updateExpense(11L, sampleExpense))
                .isInstanceOf(IdNotFound.class);

        verify(expensesRepository, never()).save(any(Expenses.class));
    }

    // ======================= delete expense =======================
    @Test
    public void ExpensesService_WithValidId_DeleteExpense() {
        when(expensesRepository.existsById(1L)).thenReturn(true);
        doNothing().when(expensesRepository).deleteById(1L); // no return since void

        expensesService.deleteExpense(1L);

        verify(expensesRepository, times(1)).deleteById(1L);
    }

    @Test
    public void ExpensesService_DeleteWithInvalidId_ThrowIdNotFound() {
        when(expensesRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> expensesService.deleteExpense(99L))
                .isInstanceOf(IdNotFound.class);

        verify(expensesRepository, never()).deleteById(any());
    }
}
