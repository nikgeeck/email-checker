package ru.lab.emailchecker.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.lab.emailchecker.domain.Counterparty;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class CounterpartyResponse {

    private Long id;
    private String name;
    private String email;
    private boolean formatValid;
    private boolean mxValid;
    private Instant checkedAt;
    private List<String> mxHosts;

    public static CounterpartyResponse from(Counterparty entity) {
        return from(entity, List.of());
    }

    public static CounterpartyResponse from(Counterparty entity, List<String> mxHosts) {
        return new CounterpartyResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.isFormatValid(),
                entity.isMxValid(),
                entity.getCheckedAt(),
                mxHosts == null ? List.of() : mxHosts
        );
    }
}
