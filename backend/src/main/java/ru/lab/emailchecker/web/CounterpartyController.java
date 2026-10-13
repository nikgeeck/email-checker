package ru.lab.emailchecker.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.lab.emailchecker.service.CounterpartyService;
import ru.lab.emailchecker.web.dto.CheckRequest;
import ru.lab.emailchecker.web.dto.CounterpartyResponse;

import java.util.List;

@RestController
@RequestMapping("/api/counterparties")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CounterpartyController {

    private final CounterpartyService service;

    @GetMapping
    public List<CounterpartyResponse> list() {
        return service.listAll();
    }

    @PostMapping("/check")
    public CounterpartyResponse check(@Valid @RequestBody CheckRequest request) {
        return service.checkAndSave(request);
    }
}
