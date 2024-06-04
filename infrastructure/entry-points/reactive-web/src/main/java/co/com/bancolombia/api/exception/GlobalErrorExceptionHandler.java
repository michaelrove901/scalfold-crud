package co.com.bancolombia.api.exception;

import co.com.bancolombia.api.exception.dto.ErrorResponse;
import co.com.bancolombia.api.exception.dto.ErrorResponse.ErrorDescription;
import co.com.bancolombia.exceptions.TechnicalException;
import co.com.bancolombia.model.exceptions.TecnicalExceptionBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import co.com.bancolombia.model.exceptions.TecnicalExceptionEnum;

import java.util.List;
import java.util.Map;

@Component
@Order(-2)
public class GlobalErrorExceptionHandler extends AbstractErrorWebExceptionHandler {


    private final Map<String, HttpStatus> httpStatusExceptionMap;
    private final String appName;
    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorExceptionHandler.class);

    public GlobalErrorExceptionHandler(DefaultErrorAttributes errorAttributes, ApplicationContext applicationContext,
                                       ServerCodecConfigurer serverCodecConfigurer,
                                       @Value("${spring.application.name}") String appName,
                                       Map<String, HttpStatus> httpStatusExceptionMap) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        this.httpStatusExceptionMap = httpStatusExceptionMap;
        this.appName = appName;
        initExceptionMap();
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

        return Mono.just(request)
                .map(this::getError)
                .flatMap(error -> Mono.error(new RuntimeException(error)))
                .onErrorResume(TechnicalException.class, this::buildErrorResponse)
                .onErrorResume(TecnicalExceptionBusiness.class, this::buildErrorResponse)
                .onErrorResume(this::buildErrorResponse)
                .cast(ErrorDescription.class)
                .map(errorResponse -> errorResponse.toBuilder().domain(request.path()).build())
                .flatMap(errorResponse -> buildResponse(errorResponse, request))
                .doOnError(throwable -> logger.error(throwable.getMessage()));
    }

    private Mono<ErrorDescription> buildErrorResponse(TechnicalException technicalException) {
        return Mono.just(ErrorDescription.builder()
                .reason(technicalException.getTechnicalErrorMessage().getMessage())
                .code(technicalException.getTechnicalErrorMessage().getCode())
                .message(technicalException.getTechnicalErrorMessage().getMessage())
                .build());
    }

    private Mono<ErrorDescription> buildErrorResponse(TecnicalExceptionBusiness businessException) {
        return Mono.just(ErrorDescription.builder()
                .reason(businessException.getErrorMessage().getMessage())
                .code(businessException.getErrorMessage().getCode())
                .message(businessException.getErrorMessage().getMessage())
                .build());
    }

    private Mono<ErrorDescription> buildErrorResponse(Throwable throwable) {
        return Mono.just(ErrorDescription.builder()
                .reason(TecnicalExceptionEnum.UNKNOWN_ERROR.getMessage())
                .code(TecnicalExceptionEnum.UNKNOWN_ERROR.getCode())
                .message(TecnicalExceptionEnum.UNKNOWN_ERROR.getMessage())
                .build());
    }

    private Mono<ServerResponse> buildResponse(ErrorDescription errorDto, final ServerRequest request) {
        var errorResponse = ErrorResponse.builder()
                .errors(List.of(errorDto))
                .build();

        HttpStatus status = httpStatusExceptionMap.get(errorResponse.getErrors().get(0).getCode());

        if (status != HttpStatus.NO_CONTENT) {
            return ServerResponse.status(status != null ? status : HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(errorResponse)
                    .doOnNext(response -> request.attributes().put("CACHE_RESPONSE_BODY",
                            errorResponse));
        } else {
            return ServerResponse.status(HttpStatus.NO_CONTENT).bodyValue("");
        }
    }


    private void initExceptionMap() {

        httpStatusExceptionMap.put("DLT001", HttpStatus.CONFLICT);
        httpStatusExceptionMap.put("DLT002", HttpStatus.CONFLICT);
        httpStatusExceptionMap.put("DLT003", HttpStatus.BAD_GATEWAY);
        httpStatusExceptionMap.put("DLT004", HttpStatus.BAD_GATEWAY);
        httpStatusExceptionMap.put("DLT005", HttpStatus.BAD_GATEWAY);
        httpStatusExceptionMap.put("DLT006", HttpStatus.BAD_GATEWAY);
        httpStatusExceptionMap.put("DLT007", HttpStatus.GATEWAY_TIMEOUT);
        httpStatusExceptionMap.put("DLT008", HttpStatus.GATEWAY_TIMEOUT);
        httpStatusExceptionMap.put("DLT009", HttpStatus.INTERNAL_SERVER_ERROR);
        httpStatusExceptionMap.put("DLT010", HttpStatus.UNAUTHORIZED);
        httpStatusExceptionMap.put("DLT011", HttpStatus.FORBIDDEN);
        httpStatusExceptionMap.put("DLT012", HttpStatus.INTERNAL_SERVER_ERROR);
        httpStatusExceptionMap.put("DLT013", HttpStatus.BAD_GATEWAY);
        httpStatusExceptionMap.put("DLT014", HttpStatus.NOT_FOUND);
        httpStatusExceptionMap.put("DLT015", HttpStatus.CONFLICT);

    }
}
