package ru.lab.emailchecker.web;

public class DuplicateEmailException extends RuntimeException {

    private final String email;

    public DuplicateEmailException(String email) {
        super("Контрагент с email " + email + " уже есть");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
