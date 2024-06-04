package co.com.bancolombia.mariadb.repository;

import co.com.bancolombia.mariadb.dto.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UsuarioRepository extends ReactiveCrudRepository<UserEntity, Long>{
    Flux<UserEntity> getByFirstNameContainingIgnoreCase(String name);

}
