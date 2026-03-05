package com.kaek.pl.repository;

import com.kaek.pl.domain.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepository extends JpaSpecificationExecutor<Expenses>, JpaRepository<Expenses, Long> {
}
