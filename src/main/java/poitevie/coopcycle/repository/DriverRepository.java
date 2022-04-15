package poitevie.coopcycle.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import poitevie.coopcycle.domain.Driver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Driver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverRepository extends ReactiveCrudRepository<Driver, Long>, DriverRepositoryInternal {
    @Query("SELECT * FROM driver entity WHERE entity.cooperative_id = :id")
    Flux<Driver> findByCooperative(Long id);

    @Query("SELECT * FROM driver entity WHERE entity.cooperative_id IS NULL")
    Flux<Driver> findAllWhereCooperativeIsNull();

    @Override
    <S extends Driver> Mono<S> save(S entity);

    @Override
    Flux<Driver> findAll();

    @Override
    Mono<Driver> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DriverRepositoryInternal {
    <S extends Driver> Mono<S> save(S entity);

    Flux<Driver> findAllBy(Pageable pageable);

    Flux<Driver> findAll();

    Mono<Driver> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Driver> findAllBy(Pageable pageable, Criteria criteria);

}
