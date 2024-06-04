package co.com.bancolombia.mariadb;


import co.com.bancolombia.mariadb.dto.UserEntity;
import co.com.bancolombia.mariadb.exception.RequestException;
import co.com.bancolombia.mariadb.repository.UsuarioRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Repository
@RequiredArgsConstructor
public class UserService implements UserRepository {

    private final UsuarioRepository repository;
    @Override
    public Mono<User> save(User user) {
        return repository.save(UserEntity
                        .builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .avatar(user.getAvatar())
                        .build())
                .flatMap(savedUserEntity -> Mono.just(User
                        .builder()
                        .id(savedUserEntity.getId())
                        .firstName(savedUserEntity.getFirstName())
                        .lastName(savedUserEntity.getLastName())
                        .email(savedUserEntity.getEmail())
                        .avatar(savedUserEntity.getAvatar())
                        .build()));
    }


    @Override
    public Flux<User> findAll() {
        return repository.findAll().map(userEntity -> User
                .builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .avatar(userEntity.getAvatar())
                .build());
    }

    @Override
    public Mono<User> update(User user) {
        return repository.findById(user.getId())
                .flatMap(userEntity -> repository.save(UserEntity
                        .builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .avatar(user.getAvatar())
                        .build()))
                .map(savedUserEntity -> User
                        .builder()
                        .id(savedUserEntity.getId())
                        .firstName(savedUserEntity.getFirstName())
                        .lastName(savedUserEntity.getLastName())
                        .email(savedUserEntity.getEmail())
                        .avatar(savedUserEntity.getAvatar())
                        .build())
                .switchIfEmpty(Mono.error(new RequestException("E-0001","User not found")));
    }
    @Override
    public Flux<User> getByName(String name) {
        return repository
                .getByFirstNameContainingIgnoreCase(name)
                .map(user -> User
                        .builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .avatar(user.getAvatar())
                        .build());
    }

    @Override
    public Mono<User> getById(Long id) {
        return repository
                .findById(id)
                .map(user -> User
                        .builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .avatar(user.getAvatar())
                        .build());
    }


}
