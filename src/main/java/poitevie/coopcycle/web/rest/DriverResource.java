package poitevie.coopcycle.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import poitevie.coopcycle.repository.DriverRepository;
import poitevie.coopcycle.service.DriverService;
import poitevie.coopcycle.service.dto.DriverDTO;
import poitevie.coopcycle.web.rest.errors.BadRequestAlertException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link poitevie.coopcycle.domain.Driver}.
 */
@RestController
@RequestMapping("/api")
public class DriverResource {

    private final Logger log = LoggerFactory.getLogger(DriverResource.class);

    private static final String ENTITY_NAME = "driver";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DriverService driverService;

    private final DriverRepository driverRepository;

    public DriverResource(DriverService driverService, DriverRepository driverRepository) {
        this.driverService = driverService;
        this.driverRepository = driverRepository;
    }

    /**
     * {@code POST  /drivers} : Create a new driver.
     *
     * @param driverDTO the driverDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new driverDTO, or with status {@code 400 (Bad Request)} if the driver has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/drivers")
    public Mono<ResponseEntity<DriverDTO>> createDriver(@Valid @RequestBody DriverDTO driverDTO) throws URISyntaxException {
        log.debug("REST request to save Driver : {}", driverDTO);
        if (driverDTO.getId() != null) {
            throw new BadRequestAlertException("A new driver cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return driverService
            .save(driverDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/drivers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /drivers/:id} : Updates an existing driver.
     *
     * @param id the id of the driverDTO to save.
     * @param driverDTO the driverDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated driverDTO,
     * or with status {@code 400 (Bad Request)} if the driverDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the driverDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/drivers/{id}")
    public Mono<ResponseEntity<DriverDTO>> updateDriver(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DriverDTO driverDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Driver : {}, {}", id, driverDTO);
        if (driverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, driverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return driverRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return driverService
                    .update(driverDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /drivers/:id} : Partial updates given fields of an existing driver, field will ignore if it is null
     *
     * @param id the id of the driverDTO to save.
     * @param driverDTO the driverDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated driverDTO,
     * or with status {@code 400 (Bad Request)} if the driverDTO is not valid,
     * or with status {@code 404 (Not Found)} if the driverDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the driverDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/drivers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DriverDTO>> partialUpdateDriver(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DriverDTO driverDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Driver partially : {}, {}", id, driverDTO);
        if (driverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, driverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return driverRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DriverDTO> result = driverService.partialUpdate(driverDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /drivers} : get all the drivers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of drivers in body.
     */
    @GetMapping("/drivers")
    public Mono<List<DriverDTO>> getAllDrivers() {
        log.debug("REST request to get all Drivers");
        return driverService.findAll().collectList();
    }

    /**
     * {@code GET  /drivers} : get all the drivers as a stream.
     * @return the {@link Flux} of drivers.
     */
    @GetMapping(value = "/drivers", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DriverDTO> getAllDriversAsStream() {
        log.debug("REST request to get all Drivers as a stream");
        return driverService.findAll();
    }

    /**
     * {@code GET  /drivers/:id} : get the "id" driver.
     *
     * @param id the id of the driverDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the driverDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/drivers/{id}")
    public Mono<ResponseEntity<DriverDTO>> getDriver(@PathVariable Long id) {
        log.debug("REST request to get Driver : {}", id);
        Mono<DriverDTO> driverDTO = driverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(driverDTO);
    }

    /**
     * {@code DELETE  /drivers/:id} : delete the "id" driver.
     *
     * @param id the id of the driverDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/drivers/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDriver(@PathVariable Long id) {
        log.debug("REST request to delete Driver : {}", id);
        return driverService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
