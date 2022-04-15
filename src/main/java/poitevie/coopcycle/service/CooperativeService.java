package poitevie.coopcycle.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poitevie.coopcycle.domain.Cooperative;
import poitevie.coopcycle.repository.CooperativeRepository;
import poitevie.coopcycle.service.dto.CooperativeDTO;
import poitevie.coopcycle.service.mapper.CooperativeMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Cooperative}.
 */
@Service
@Transactional
public class CooperativeService {

    private final Logger log = LoggerFactory.getLogger(CooperativeService.class);

    private final CooperativeRepository cooperativeRepository;

    private final CooperativeMapper cooperativeMapper;

    public CooperativeService(CooperativeRepository cooperativeRepository, CooperativeMapper cooperativeMapper) {
        this.cooperativeRepository = cooperativeRepository;
        this.cooperativeMapper = cooperativeMapper;
    }

    /**
     * Save a cooperative.
     *
     * @param cooperativeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CooperativeDTO> save(CooperativeDTO cooperativeDTO) {
        log.debug("Request to save Cooperative : {}", cooperativeDTO);
        return cooperativeRepository.save(cooperativeMapper.toEntity(cooperativeDTO)).map(cooperativeMapper::toDto);
    }

    /**
     * Update a cooperative.
     *
     * @param cooperativeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CooperativeDTO> update(CooperativeDTO cooperativeDTO) {
        log.debug("Request to save Cooperative : {}", cooperativeDTO);
        return cooperativeRepository.save(cooperativeMapper.toEntity(cooperativeDTO).setIsPersisted()).map(cooperativeMapper::toDto);
    }

    /**
     * Partially update a cooperative.
     *
     * @param cooperativeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CooperativeDTO> partialUpdate(CooperativeDTO cooperativeDTO) {
        log.debug("Request to partially update Cooperative : {}", cooperativeDTO);

        return cooperativeRepository
            .findById(cooperativeDTO.getId())
            .map(existingCooperative -> {
                cooperativeMapper.partialUpdate(existingCooperative, cooperativeDTO);

                return existingCooperative;
            })
            .flatMap(cooperativeRepository::save)
            .map(cooperativeMapper::toDto);
    }

    /**
     * Get all the cooperatives.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CooperativeDTO> findAll() {
        log.debug("Request to get all Cooperatives");
        return cooperativeRepository.findAll().map(cooperativeMapper::toDto);
    }

    /**
     * Returns the number of cooperatives available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return cooperativeRepository.count();
    }

    /**
     * Get one cooperative by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CooperativeDTO> findOne(String id) {
        log.debug("Request to get Cooperative : {}", id);
        return cooperativeRepository.findById(id).map(cooperativeMapper::toDto);
    }

    /**
     * Delete the cooperative by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Cooperative : {}", id);
        return cooperativeRepository.deleteById(id);
    }
}
