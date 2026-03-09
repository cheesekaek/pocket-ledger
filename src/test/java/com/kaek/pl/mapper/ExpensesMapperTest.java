package com.kaek.pl.mapper;

import com.kaek.pl.domain.dto.ExpensesDto;
import com.kaek.pl.domain.entity.Expenses;
import com.kaek.pl.domain.mapper.ExpensesMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpensesMapperTest {

    private ExpensesMapper expensesMapper = new ExpensesMapper();

    private Expenses expense;
    private ExpensesDto expensesDto;

    @BeforeEach
    public void setSampleEntity() {
        expense = Expenses.builder()
                .description("Lunch")
                .category("Food")
                .amount(22.50)
                .date(LocalDate.of(2026,3,3))
                .build();

        expensesDto = ExpensesDto.builder()
                .description("Lunch")
                .category("Food")
                .amount(22.50)
                .date(LocalDate.of(2026,3,3))
                .build();
    }

    @Test
    public void ExpensesMapper_ToExpDto_ReturnDto() {
        ExpensesDto expensesDto = expensesMapper.toExpDto(expense);

        assertThat(expensesDto.getDescription()).isEqualTo("Lunch");
        assertThat(expensesDto.getCategory()).isEqualTo("Food");
        assertThat(expensesDto.getAmount()).isEqualTo(22.50);
        assertThat(expensesDto.getDate()).isEqualTo(LocalDate.of(2026, 3, 3));
    }

    @Test
    public void ExpensesMapper_ToExpEntity_ReturnEntity() {
        Expenses expenses = expensesMapper.toExpEntity(expensesDto);

        assertThat(expenses.getDescription()).isEqualTo("Lunch");
        assertThat(expenses.getCategory()).isEqualTo("Food");
        assertThat(expenses.getAmount()).isEqualTo(22.50);
        assertThat(expenses.getDate()).isEqualTo(LocalDate.of(2026, 3, 3));
    }

    // round trip
    @Test
    public void ExpensesMapper_ToExpDto_ToExpEntity_ToExpDto_ReturnEntity() {
        Expenses expenses = expensesMapper.toExpEntity(expensesMapper.toExpDto(expense));

        assertThat(expenses.getDescription()).isEqualTo("Lunch");
        assertThat(expenses.getCategory()).isEqualTo("Food");
        assertThat(expenses.getAmount()).isEqualTo(22.50);
        assertThat(expenses.getDate()).isEqualTo(LocalDate.of(2026, 3, 3));
    }

}
