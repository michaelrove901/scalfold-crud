package co.com.bancolombia.api.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ErrorResponse {

    List<ErrorDescription> errors;

    public List<ErrorDescription> getErrors() {
        return errors;
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class ErrorDescription {


        private String reason;


        private String domain;


        private String code;


        private String message;

    }
}