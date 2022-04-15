package poitevie.coopcycle.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import poitevie.coopcycle.domain.Cart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartRepository extends ReactiveCrudRepository<Cart, Long>, CartRepositoryInternal {
    @Query("SELECT * FROM cart entity WHERE entity.command_id = :id")
    Flux<Cart> findByCommand(Long id);

    @Query("SELECT * FROM cart entity WHERE entity.command_id IS NULL")
    Flux<Cart> findAllWhereCommandIsNull();

    @Query("SELECT * FROM cart entity WHERE entity.client_id = :id")
    Flux<Cart> findByClient(Long id);

    @Query("SELECT * FROM cart entity WHERE entity.client_id IS NULL")
    Flux<Cart> findAllWhereClientIsNull();

    @Query("SELECT * FROM cart entity WHERE entity.shop_id = :id")
    Flux<Cart> findByShop(Long id);

    @Query("SELECT * FROM cart entity WHERE entity.shop_id IS NULL")
    Flux<Cart> findAllWhereShopIsNull();

    @Override
    <S extends Cart> Mono<S> save(S entity);

    @Override
    Flux<Cart> findAll();

    @Override
    Mono<Cart> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CartRepositoryInternal {
    <S extends Cart> Mono<S> save(S entity);

    Flux<Cart> findAllBy(Pageable pageable);

    Flux<Cart> findAll();

    Mono<Cart> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Cart> findAllBy(Pageable pageable, Criteria criteria);

}
