package ru.lab.emailchecker.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidateResponse {

    private String email;
    private boolean formatValid;
    private boolean mxValid;
    private List<String> mxHosts;
}
