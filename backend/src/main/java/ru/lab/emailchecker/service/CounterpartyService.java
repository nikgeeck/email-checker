package ru.lab.emailchecker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lab.emailchecker.domain.Counterparty;
import ru.lab.emailchecker.domain.CounterpartyRepository;
import ru.lab.emailchecker.validator.EmailValidatorClient;
import ru.lab.emailchecker.validator.ValidateResponse;
import ru.lab.emailchecker.web.DuplicateEmailException;
import ru.lab.emailchecker.web.dto.CheckRequest;
import ru.lab.emailchecker.web.dto.CounterpartyResponse;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CounterpartyService {

    private final CounterpartyRepository repository;
    private final EmailValidatorClient validatorClient;

    @Transactional(readOnly = true)
    public List<CounterpartyResponse> listAll() {
        return repository.findAllByOrderByCheckedAtDesc().stream()
                .map(CounterpartyResponse::from)
                .toList();
    }

    @Transactional
    public CounterpartyResponse checkAndSave(CheckRequest request) {
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        if (repository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateEmailException(email);
        }

        ValidateResponse validation = validatorClient.validate(email);

        Counterparty entity = new Counterparty();
        entity.setName(request.getName().trim());
        entity.setEmail(email);
        entity.setFormatValid(validation.isFormatValid());
        entity.setMxValid(validation.isMxValid());
        entity.setCheckedAt(Instant.now());

        Counterparty saved = repository.save(entity);
        return CounterpartyResponse.from(saved, validation.getMxHosts());
    }
}
