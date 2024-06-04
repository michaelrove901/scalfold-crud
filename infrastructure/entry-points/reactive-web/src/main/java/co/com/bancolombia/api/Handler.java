package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.UserCreate;
import co.com.bancolombia.api.dto.UserRequest;
import co.com.bancolombia.api.dto.UserResponse;
import co.com.bancolombia.api.dto.UserUpdate;
import co.com.bancolombia.model.exceptions.TecnicalExceptionBusiness;
import co.com.bancolombia.model.exceptions.TecnicalExceptionEnum;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.usercase.UserCaseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {
private  final UserCaseUseCase userCaseUseCase;
private final Validator validator;
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserCreate.class)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new TecnicalExceptionBusiness(TecnicalExceptionEnum.BAD_REQUEST))))
                .flatMap(request -> {
                    Errors errors = new BeanPropertyBindingResult(request, UserCreate.class.getName());
                    validator.validate(request, errors);
                    if (errors.hasErrors()) {
                        return Mono.error(new TecnicalExceptionBusiness(TecnicalExceptionEnum.BAD_REQUEST));
                    } else {
                        return Mono.just(request);
                    }
                })
                .map(request -> User.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .avatar(request.getAvatar())
                        .build())
                .flatMap(userCaseUseCase::save)
                .map(Handler::mapUserToUserResponse)
                .flatMap(userResponse -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userResponse));
    }


    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .body(userCaseUseCase
                        .findAll()
                        .map(Handler::mapUserToUserResponse), UserResponse.class);
    }
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserUpdate.class)
                .map(request -> User.builder()
                        .id(request.getId())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .avatar(request.getAvatar())
                        .build())
                .flatMap(userCaseUseCase::update)
                .map(Handler::mapUserToUserResponse)
                .flatMap(userResponse -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userResponse));
    }

    public Mono<ServerResponse> getByName(ServerRequest serverRequest) {
        String name = serverRequest.queryParam("name").orElse("");
        return userCaseUseCase
                .getByName(name)
                .collectList()
                .flatMap(users -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(users.stream().map(Handler::mapUserToUserResponse).collect(Collectors.toList())));
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        String idStr = serverRequest.queryParam("id").orElse("");
        Long id = Long.parseLong(idStr);
        return userCaseUseCase
                .getById(id)
                .map(Handler::mapUserToUserResponse)
                .flatMap(userResponse -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userResponse))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    private static UserResponse mapUserToUserResponse(User user) {
        return UserResponse
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .build();
    }
}
