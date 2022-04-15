package poitevie.coopcycle.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import poitevie.coopcycle.domain.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Client entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientRepository extends ReactiveCrudRepository<Client, Long>, ClientRepositoryInternal {
    @Override
    <S extends Client> Mono<S> save(S entity);

    @Override
    Flux<Client> findAll();

    @Override
    Mono<Client> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ClientRepositoryInternal {
    <S extends Client> Mono<S> save(S entity);

    Flux<Client> findAllBy(Pageable pageable);

    Flux<Client> findAll();

    Mono<Client> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Client> findAllBy(Pageable pageable, Criteria criteria);

}
