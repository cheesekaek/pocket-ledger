package com.kaek.pl.controller;

import com.kaek.pl.domain.dto.ExpensesDto;
import com.kaek.pl.domain.entity.Expenses;
import com.kaek.pl.domain.mapper.ExpensesMapper;
import com.kaek.pl.exception.GlobalExceptionsHandler;
import com.kaek.pl.exception.IdNotFound;
import com.kaek.pl.exception.InvalidInput;
import com.kaek.pl.service.ExpensesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ExpensesController.class, GlobalExceptionsHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ExpensesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpensesService expensesService;

    @MockitoBean
    private ExpensesMapper expensesMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Expenses sampleExpense;

    private Expenses expense;
    private ExpensesDto expensesDto;

    @BeforeEach
    public void setSampleEntity() {
        expense = Expenses.builder()
                .id(1L)
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

    // ============================= POST =============================
    @Test
    public void ExpensesController_CreateExpense_ReturnCreatedDto() throws Exception {
        when(expensesMapper.toExpEntity(any())).thenReturn(expense);
        when(expensesService.saveExpense(any())).thenReturn(expense);
        when(expensesMapper.toExpDto(any())).thenReturn(expensesDto);

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expensesDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Lunch"))
                .andExpect(jsonPath("$.category").value("Food"))
                .andExpect(jsonPath("$.amount").value(22.50))
                .andDo(MockMvcResultHandlers.print());
    }

    // empty required fields
    @Test
    public void ExpensesController_CreateExpense_InvalidBody_Return400() throws Exception {
        ExpensesDto invalid = ExpensesDto.builder().build();

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    public void ExpensesController_CreateExpense_ServiceThrowsInvalidInput_Return400() throws Exception {
        when(expensesMapper.toExpEntity(any())).thenReturn(expense);
        when(expensesService.saveExpense(any())).thenThrow(new InvalidInput("Amount must be positive"));

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expensesDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount must be positive"));
    }

    // ============================= GET =============================
    @Test
    public void ExpensesController_GetAllExpenses_NoFilters_ReturnPage() throws Exception {
        Page<Expenses> page = new PageImpl<>(List.of(expense));

        when(expensesService.getAllExpenses(isNull(), isNull(), isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(page);
        when(expensesMapper.toExpDto(any())).thenReturn(expensesDto);

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].description").value("Lunch"));
    }

    @Test
    public void ExpensesController_GetAllExpenses_WithDescriptionFilter_ReturnPage() throws Exception {
        Page<Expenses> page = new PageImpl<>(List.of(expense));

        when(expensesService.getAllExpenses(eq("Lunch"), isNull(), isNull(), isNull(), isNull(), any()))
                .thenReturn(page);
        when(expensesMapper.toExpDto(any())).thenReturn(expensesDto);

        mockMvc.perform(get("/api/expenses").param("description", "Lunch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description").value("Lunch"));

        verify(expensesService).getAllExpenses(eq("Lunch"), isNull(), isNull(), isNull(), isNull(), any());
    }

    @Test
    public void ExpensesController_GetAllExpenses_WithCategoryFilter_ReturnPage() throws Exception {
        Page<Expenses> page = new PageImpl<>(List.of(expense));

        when(expensesService.getAllExpenses(isNull(), eq("Food"), isNull(), isNull(), isNull(), any()))
                .thenReturn(page);
        when(expensesMapper.toExpDto(any())).thenReturn(expensesDto);

        mockMvc.perform(get("/api/expenses").param("category", "Food"))
                .andExpect(status().isOk());

        verify(expensesService).getAllExpenses(isNull(), eq("Food"), isNull(), isNull(), isNull(), any());
    }

    @Test
    public void ExpensesController_GetAllExpenses_WithDateRange_ReturnPage() throws Exception {
        Page<Expenses> page = new PageImpl<>(List.of(expense));
        LocalDate after = LocalDate.of(2026, 1, 1);
        LocalDate before = LocalDate.of(2026, 3, 31);

        when(expensesService.getAllExpenses(isNull(), isNull(), eq(after), eq(before), isNull(), any()))
                .thenReturn(page);
        when(expensesMapper.toExpDto(any())).thenReturn(expensesDto);

        mockMvc.perform(get("/api/expenses")
                        .param("dateAfter", "2026-01-01")
                        .param("dateBefore", "2026-03-31"))
                .andExpect(status().isOk());

        verify(expensesService).getAllExpenses(isNull(), isNull(), eq(after), eq(before), isNull(), any());
    }

    @Test
    public void ExpensesController_GetAllExpenses_WithExactDate_ReturnPage() throws Exception {
        Page<Expenses> page = new PageImpl<>(List.of(expense));
        LocalDate date = LocalDate.of(2026, 3, 3);

        when(expensesService.getAllExpenses(isNull(), isNull(), isNull(), isNull(), eq(date), any()))
                .thenReturn(page);
        when(expensesMapper.toExpDto(any())).thenReturn(expensesDto);

        mockMvc.perform(get("/api/expenses").param("date", "2026-03-03"))
                .andExpect(status().isOk());
    }

    @Test
    public void ExpensesController_GetAllExpenses_EmptyResult_ReturnEmptyPage() throws Exception {
        when(expensesService.getAllExpenses(any(), any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    public void ExpensesController_GetAllExpenses_MalformedDate_Return400() throws Exception {
        mockMvc.perform(get("/api/expenses").param("dateAfter", "not-a-date"))
                .andExpect(status().isBadRequest());
    }

    // ============================= PUT =============================

    @Test
    public void ExpensesController_UpdateExpense_ReturnUpdatedDto() throws Exception {
        when(expensesMapper.toExpEntity(any())).thenReturn(expense);
        when(expensesService.updateExpense(eq(1L), any())).thenReturn(expense);
        when(expensesMapper.toExpDto(any())).thenReturn(expensesDto);

        mockMvc.perform(put("/api/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expensesDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Lunch"))
                .andExpect(jsonPath("$.amount").value(22.50));
    }

    @Test
    public void ExpensesController_UpdateExpense_IdNotFound_Return404() throws Exception {
        when(expensesMapper.toExpEntity(any())).thenReturn(expense);
        when(expensesService.updateExpense(eq(99L), any()))
                .thenThrow(new IdNotFound(99L));

        mockMvc.perform(put("/api/expenses/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expensesDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("The id 99 cannot be found"));
    }

    @Test
    public void ExpensesController_UpdateExpense_InvalidBody_Return400() throws Exception {
        ExpensesDto invalid = ExpensesDto.builder().build();

        mockMvc.perform(put("/api/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    public void ExpensesController_UpdateExpense_NonNumericId_Return400() throws Exception {
        mockMvc.perform(put("/api/expenses/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expensesDto)))
                .andExpect(status().isBadRequest());
    }


    // ============================= DELETE =============================
    @Test
    public void ExpensesController_DeleteExpense_Return204() throws Exception {
        doNothing().when(expensesService).deleteExpense(1L);

        mockMvc.perform(delete("/api/expenses/1"))
                .andExpect(status().isNoContent());

        verify(expensesService).deleteExpense(1L);
    }

    @Test
    public void ExpensesController_DeleteExpense_IdNotFound_Return404() throws Exception {
        doThrow(new IdNotFound(99L))
                .when(expensesService).deleteExpense(99L);

        mockMvc.perform(delete("/api/expenses/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("The id 99 cannot be found"));
    }

    @Test
    public void ExpensesController_DeleteExpense_NonNumericId_Return400() throws Exception {
        mockMvc.perform(delete("/api/expenses/abc"))
                .andExpect(status().isBadRequest());
    }
}

