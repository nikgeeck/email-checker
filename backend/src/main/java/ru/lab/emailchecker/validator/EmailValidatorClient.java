package ru.lab.emailchecker.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EmailValidatorClient {

    private final RestTemplate restTemplate;
    private final String validateUrl;

    public EmailValidatorClient(
            RestTemplate restTemplate,
            @Value("${email-validator.url}") String baseUrl
    ) {
        this.restTemplate = restTemplate;
        this.validateUrl = baseUrl.replaceAll("/$", "") + "/validate";
    }

    public ValidateResponse validate(String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ValidateRequest> entity = new HttpEntity<>(new ValidateRequest(email), headers);

        ResponseEntity<ValidateResponse> response = restTemplate.postForEntity(
                validateUrl,
                entity,
                ValidateResponse.class
        );
        ValidateResponse body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("Empty response from email validator");
        }
        return body;
    }
}
