package com.kaek.pl.repository;

import com.kaek.pl.domain.entity.Expenses;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ExpensesSpecification {

    // filter by category - exact category
    public static Specification<Expenses> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category);
    }

    // filter by desc - similar descriptions
    public static Specification<Expenses> hasDesc(String description) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get(description)), "%" + description + "%");
    }

    // filter by date - exact date
    public static Specification<Expenses> hasDate(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("date"), date);
    }

    // filter by dates - b/w two dates
    public static Specification<Expenses> hasDates(LocalDate dateAfter, LocalDate dateBefore) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("date"), dateAfter, dateBefore);
    }
}
