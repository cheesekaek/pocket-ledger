package com.kaek.pl.domain.mapper;

import com.kaek.pl.domain.dto.ExpensesDto;
import com.kaek.pl.domain.entity.Expenses;
import org.springframework.stereotype.Component;

@Component
public class ExpensesMapper {
    public ExpensesDto toExpDto(Expenses expense) {
        ExpensesDto dto = new ExpensesDto();
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setCategory(expense.getCategory());
        return dto;
    }

    public Expenses toExpEntity(ExpensesDto dto) {
        Expenses expense = new Expenses();
        expense.setDescription(dto.getDescription());
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setCategory(dto.getCategory());
        return expense;
    }
}
