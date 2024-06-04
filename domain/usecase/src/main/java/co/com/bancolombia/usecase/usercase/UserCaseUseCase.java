package co.com.bancolombia.usecase.usercase;

import co.com.bancolombia.model.exceptions.TecnicalExceptionBusiness;
import co.com.bancolombia.model.exceptions.TecnicalExceptionEnum;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserCaseUseCase {
    private final UserRepository userRepository;
    public Flux<User> findAll() {
        return userRepository.findAll();
    }
    public Mono<User> save(User user) {
        return userRepository.save(user).onErrorMap(Exception.class,error -> new TecnicalExceptionBusiness(error, TecnicalExceptionEnum.BAD_REQUEST));
    }

    public Mono<User> update(User user) {
        return userRepository.update(user).thenReturn(user);
    }
    public Flux<User> getByName(String name) {
        return userRepository.getByName(name);
    }
    public Mono<User> getById(Long id) {
        return userRepository.getById(id);
    }
}
