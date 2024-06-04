package co.com.bancolombia.model.exceptions;
import lombok.Getter;

@Getter
public class TecnicalExceptionBusiness extends RuntimeException{
    private final TecnicalExceptionEnum errorMessage;

    public TecnicalExceptionBusiness(Throwable cause, TecnicalExceptionEnum errorMessage) {
        super(errorMessage.getMessage(), cause);
        this.errorMessage = errorMessage;
    }

    public TecnicalExceptionBusiness(TecnicalExceptionEnum errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}