package co.com.bancolombia.mariadb;

import co.com.bancolombia.mariadb.dto.UserEntity;
import co.com.bancolombia.mariadb.repository.UsuarioRepository;
import co.com.bancolombia.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UsuarioRepository usuarioRepository;
    private UserService userService;

    @BeforeEach
    public void setup() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        userService = new UserService(usuarioRepository);
    }

    @Test
    public void testSave() {
        User user = new User();
        UserEntity userEntity = new UserEntity();
        when(usuarioRepository.save(any(UserEntity.class))).thenReturn(Mono.just(userEntity));

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        StepVerifier.create(userService.save(user))
                .assertNext(savedUser -> {
                    verify(usuarioRepository).save(captor.capture());
                    UserEntity captured = captor.getValue();
                     assertEquals(user.getId(), captured.getId());
                     assertEquals(user.getFirstName(), captured.getFirstName());

                })
                .verifyComplete();
    }

    @Test
    public void testFindAll() {
        when(usuarioRepository.findAll()).thenReturn(Flux.just(new UserEntity()));

        StepVerifier.create(userService.findAll())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test.user@example.com");
        user.setAvatar("test-avatar");

        UserEntity updatedUserEntity = new UserEntity();
        updatedUserEntity.setId(user.getId());
        updatedUserEntity.setFirstName(user.getFirstName());
        updatedUserEntity.setLastName(user.getLastName());
        updatedUserEntity.setEmail(user.getEmail());
        updatedUserEntity.setAvatar(user.getAvatar());

        when(usuarioRepository.findById(any(Long.class))).thenReturn(Mono.just(new UserEntity()));
        when(usuarioRepository.save(any(UserEntity.class))).thenReturn(Mono.just(updatedUserEntity));

        StepVerifier.create(userService.update(user))
                .expectNextMatches(updatedUser ->
                        updatedUser.getId().equals(user.getId()) &&
                                updatedUser.getFirstName().equals(user.getFirstName()) &&
                                updatedUser.getLastName().equals(user.getLastName()) &&
                                updatedUser.getEmail().equals(user.getEmail()) &&
                                updatedUser.getAvatar().equals(user.getAvatar())
                )
                .verifyComplete();
    }

    @Test
    public void testGetByName() {
        String name = "test";
        when(usuarioRepository.getByFirstNameContainingIgnoreCase(name)).thenReturn(Flux.just(new UserEntity()));

        StepVerifier.create(userService.getByName(name))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void testGetById() {
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Mono.just(new UserEntity()));

        StepVerifier.create(userService.getById(id))
                .expectNextCount(1)
                .verifyComplete();
    }
}