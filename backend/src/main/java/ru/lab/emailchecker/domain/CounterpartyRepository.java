package ru.lab.emailchecker.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CounterpartyRepository extends JpaRepository<Counterparty, Long> {

    List<Counterparty> findAllByOrderByCheckedAtDesc();

    boolean existsByEmailIgnoreCase(String email);
}
