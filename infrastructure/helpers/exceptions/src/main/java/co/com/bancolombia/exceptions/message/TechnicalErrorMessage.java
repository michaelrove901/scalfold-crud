package co.com.bancolombia.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalErrorMessage {

    NOT_ACCESS_TO_REQUESTED_INFORMATION
            ("DLT001", "The consumer does not have access to the requested information."),
    ERROR_CONVERTING_STRING_TO_OBJECT
            ("DLT002", "Error converting string to object");



    private final String code;
    private final String message;
}
