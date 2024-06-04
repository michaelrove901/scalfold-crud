package co.com.bancolombia.model.user.gateways;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User user);
    Flux<User> findAll();

    Mono<User> update(User user);

    Flux<User> getByName(String name);

    Mono<User> getById(Long id);
}
