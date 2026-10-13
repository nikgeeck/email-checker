package ru.lab.emailchecker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "counterparties")
@Getter
@Setter
public class Counterparty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(name = "format_valid", nullable = false)
    private boolean formatValid;

    @Column(name = "mx_valid", nullable = false)
    private boolean mxValid;

    @Column(name = "checked_at", nullable = false)
    private Instant checkedAt;
}
