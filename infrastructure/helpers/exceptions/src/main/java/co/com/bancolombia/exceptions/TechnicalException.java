package co.com.bancolombia.exceptions;

import co.com.bancolombia.exceptions.message.TechnicalErrorMessage;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {

    private final TechnicalErrorMessage technicalErrorMessage;

    public TechnicalException(Throwable cause, TechnicalErrorMessage technicalErrorMessage) {
        super(technicalErrorMessage.getMessage(), cause);
        this.technicalErrorMessage = technicalErrorMessage;
    }

    public TechnicalException(TechnicalErrorMessage technicalExceptionEnum) {
        super(technicalExceptionEnum.getMessage());
        this.technicalErrorMessage = technicalExceptionEnum;
    }
}