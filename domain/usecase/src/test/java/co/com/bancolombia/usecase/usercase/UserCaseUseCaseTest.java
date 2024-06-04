package co.com.bancolombia.usecase.usercase;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserCaseUseCaseTest {
    private UserRepository userRepository;
    private UserCaseUseCase userCaseUseCase;

    @BeforeEach
    public void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        userCaseUseCase = new UserCaseUseCase(userRepository);
    }

    @Test
    public void testFindAll() {
        when(userRepository.findAll()).thenReturn(Flux.just(new User()));

        StepVerifier.create(userCaseUseCase.findAll())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void testSave() {
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(userCaseUseCase.save(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    public void testUpdate() {
        User user = new User();
        when(userRepository.update(any(User.class))).thenReturn(Mono.empty());

        StepVerifier.create(userCaseUseCase.update(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    public void testGetByName() {
        String name = "test";
        when(userRepository.getByName(name)).thenReturn(Flux.just(new User()));

        StepVerifier.create(userCaseUseCase.getByName(name))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void testGetById() {
        Long id = 1L;
        when(userRepository.getById(id)).thenReturn(Mono.just(new User()));

        StepVerifier.create(userCaseUseCase.getById(id))
                .expectNextCount(1)
                .verifyComplete();
    }
}