package org.projects.customerservice.api.handler;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {

    private String message;
    private String code;
    private List<ValidationError> validationErrors;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ValidationError {

        private String field;
        private String code;
        private String message;
    }
}
