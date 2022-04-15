package poitevie.coopcycle.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import poitevie.coopcycle.domain.Command;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Command entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandRepository extends ReactiveCrudRepository<Command, Long>, CommandRepositoryInternal {
    @Query("SELECT * FROM command entity WHERE entity.client_id = :id")
    Flux<Command> findByClient(Long id);

    @Query("SELECT * FROM command entity WHERE entity.client_id IS NULL")
    Flux<Command> findAllWhereClientIsNull();

    @Query("SELECT * FROM command entity WHERE entity.id not in (select cart_id from cart)")
    Flux<Command> findAllWhereCartIsNull();

    @Query("SELECT * FROM command entity WHERE entity.driver_id = :id")
    Flux<Command> findByDriver(Long id);

    @Query("SELECT * FROM command entity WHERE entity.driver_id IS NULL")
    Flux<Command> findAllWhereDriverIsNull();

    @Override
    <S extends Command> Mono<S> save(S entity);

    @Override
    Flux<Command> findAll();

    @Override
    Mono<Command> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CommandRepositoryInternal {
    <S extends Command> Mono<S> save(S entity);

    Flux<Command> findAllBy(Pageable pageable);

    Flux<Command> findAll();

    Mono<Command> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Command> findAllBy(Pageable pageable, Criteria criteria);

}
