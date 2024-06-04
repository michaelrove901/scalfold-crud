package co.com.bancolombia.model.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TecnicalExceptionEnum {
    TECHNICAL_S3_EXCEPTION("COT0606", "An error occurred while trying to get S3 object"),
    BAD_REQUEST("COT0400", "Invalid body"),
    NOT_ACCESS("COT0401", "Access not allowed"),
    UNKNOWN_ERROR("COT0500", "Internal server error"),
    CONFLICT_SSL("COT0506", "There is an error when configuring ssl");

    private final String code;
    private final String message;

    public static TecnicalExceptionEnum getByError(String error) {
        for (TecnicalExceptionEnum err : values()) {
            if (err.code.contains(error)) {
                return err;
            }
        }
        return TecnicalExceptionEnum.UNKNOWN_ERROR;
    }
}
