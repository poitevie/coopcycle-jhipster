package poitevie.coopcycle.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import poitevie.coopcycle.domain.Shop;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Shop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopRepository extends ReactiveCrudRepository<Shop, Long>, ShopRepositoryInternal {
    @Override
    <S extends Shop> Mono<S> save(S entity);

    @Override
    Flux<Shop> findAll();

    @Override
    Mono<Shop> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ShopRepositoryInternal {
    <S extends Shop> Mono<S> save(S entity);

    Flux<Shop> findAllBy(Pageable pageable);

    Flux<Shop> findAll();

    Mono<Shop> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Shop> findAllBy(Pageable pageable, Criteria criteria);

}
