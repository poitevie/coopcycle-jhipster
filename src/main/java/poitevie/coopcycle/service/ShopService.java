package poitevie.coopcycle.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poitevie.coopcycle.domain.Shop;
import poitevie.coopcycle.repository.ShopRepository;
import poitevie.coopcycle.service.dto.ShopDTO;
import poitevie.coopcycle.service.mapper.ShopMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Shop}.
 */
@Service
@Transactional
public class ShopService {

    private final Logger log = LoggerFactory.getLogger(ShopService.class);

    private final ShopRepository shopRepository;

    private final ShopMapper shopMapper;

    public ShopService(ShopRepository shopRepository, ShopMapper shopMapper) {
        this.shopRepository = shopRepository;
        this.shopMapper = shopMapper;
    }

    /**
     * Save a shop.
     *
     * @param shopDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ShopDTO> save(ShopDTO shopDTO) {
        log.debug("Request to save Shop : {}", shopDTO);
        return shopRepository.save(shopMapper.toEntity(shopDTO)).map(shopMapper::toDto);
    }

    /**
     * Update a shop.
     *
     * @param shopDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ShopDTO> update(ShopDTO shopDTO) {
        log.debug("Request to save Shop : {}", shopDTO);
        return shopRepository.save(shopMapper.toEntity(shopDTO)).map(shopMapper::toDto);
    }

    /**
     * Partially update a shop.
     *
     * @param shopDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ShopDTO> partialUpdate(ShopDTO shopDTO) {
        log.debug("Request to partially update Shop : {}", shopDTO);

        return shopRepository
            .findById(shopDTO.getId())
            .map(existingShop -> {
                shopMapper.partialUpdate(existingShop, shopDTO);

                return existingShop;
            })
            .flatMap(shopRepository::save)
            .map(shopMapper::toDto);
    }

    /**
     * Get all the shops.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ShopDTO> findAll() {
        log.debug("Request to get all Shops");
        return shopRepository.findAll().map(shopMapper::toDto);
    }

    /**
     * Returns the number of shops available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return shopRepository.count();
    }

    /**
     * Get one shop by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ShopDTO> findOne(Long id) {
        log.debug("Request to get Shop : {}", id);
        return shopRepository.findById(id).map(shopMapper::toDto);
    }

    /**
     * Delete the shop by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Shop : {}", id);
        return shopRepository.deleteById(id);
    }
}
