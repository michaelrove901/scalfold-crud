package co.com.bancolombia.mariadb.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class RequestException extends RuntimeException {
    private String code;

    public RequestException(String code, String message) {
        super(message);
        this.code = code;
    }
}
