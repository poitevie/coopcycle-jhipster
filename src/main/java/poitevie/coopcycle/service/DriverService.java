package poitevie.coopcycle.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poitevie.coopcycle.domain.Driver;
import poitevie.coopcycle.repository.DriverRepository;
import poitevie.coopcycle.service.dto.DriverDTO;
import poitevie.coopcycle.service.mapper.DriverMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Driver}.
 */
@Service
@Transactional
public class DriverService {

    private final Logger log = LoggerFactory.getLogger(DriverService.class);

    private final DriverRepository driverRepository;

    private final DriverMapper driverMapper;

    public DriverService(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    /**
     * Save a driver.
     *
     * @param driverDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DriverDTO> save(DriverDTO driverDTO) {
        log.debug("Request to save Driver : {}", driverDTO);
        return driverRepository.save(driverMapper.toEntity(driverDTO)).map(driverMapper::toDto);
    }

    /**
     * Update a driver.
     *
     * @param driverDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DriverDTO> update(DriverDTO driverDTO) {
        log.debug("Request to save Driver : {}", driverDTO);
        return driverRepository.save(driverMapper.toEntity(driverDTO)).map(driverMapper::toDto);
    }

    /**
     * Partially update a driver.
     *
     * @param driverDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DriverDTO> partialUpdate(DriverDTO driverDTO) {
        log.debug("Request to partially update Driver : {}", driverDTO);

        return driverRepository
            .findById(driverDTO.getId())
            .map(existingDriver -> {
                driverMapper.partialUpdate(existingDriver, driverDTO);

                return existingDriver;
            })
            .flatMap(driverRepository::save)
            .map(driverMapper::toDto);
    }

    /**
     * Get all the drivers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DriverDTO> findAll() {
        log.debug("Request to get all Drivers");
        return driverRepository.findAll().map(driverMapper::toDto);
    }

    /**
     * Returns the number of drivers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return driverRepository.count();
    }

    /**
     * Get one driver by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DriverDTO> findOne(Long id) {
        log.debug("Request to get Driver : {}", id);
        return driverRepository.findById(id).map(driverMapper::toDto);
    }

    /**
     * Delete the driver by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Driver : {}", id);
        return driverRepository.deleteById(id);
    }
}
