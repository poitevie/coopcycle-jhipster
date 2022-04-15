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
import poitevie.coopcycle.repository.CooperativeRepository;
import poitevie.coopcycle.service.CooperativeService;
import poitevie.coopcycle.service.dto.CooperativeDTO;
import poitevie.coopcycle.web.rest.errors.BadRequestAlertException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link poitevie.coopcycle.domain.Cooperative}.
 */
@RestController
@RequestMapping("/api")
public class CooperativeResource {

    private final Logger log = LoggerFactory.getLogger(CooperativeResource.class);

    private static final String ENTITY_NAME = "cooperative";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CooperativeService cooperativeService;

    private final CooperativeRepository cooperativeRepository;

    public CooperativeResource(CooperativeService cooperativeService, CooperativeRepository cooperativeRepository) {
        this.cooperativeService = cooperativeService;
        this.cooperativeRepository = cooperativeRepository;
    }

    /**
     * {@code POST  /cooperatives} : Create a new cooperative.
     *
     * @param cooperativeDTO the cooperativeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cooperativeDTO, or with status {@code 400 (Bad Request)} if the cooperative has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cooperatives")
    public Mono<ResponseEntity<CooperativeDTO>> createCooperative(@Valid @RequestBody CooperativeDTO cooperativeDTO)
        throws URISyntaxException {
        log.debug("REST request to save Cooperative : {}", cooperativeDTO);
        if (cooperativeDTO.getId() != null) {
            throw new BadRequestAlertException("A new cooperative cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return cooperativeService
            .save(cooperativeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/cooperatives/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /cooperatives/:id} : Updates an existing cooperative.
     *
     * @param id the id of the cooperativeDTO to save.
     * @param cooperativeDTO the cooperativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cooperativeDTO,
     * or with status {@code 400 (Bad Request)} if the cooperativeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cooperativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cooperatives/{id}")
    public Mono<ResponseEntity<CooperativeDTO>> updateCooperative(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody CooperativeDTO cooperativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cooperative : {}, {}", id, cooperativeDTO);
        if (cooperativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cooperativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cooperativeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return cooperativeService
                    .update(cooperativeDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /cooperatives/:id} : Partial updates given fields of an existing cooperative, field will ignore if it is null
     *
     * @param id the id of the cooperativeDTO to save.
     * @param cooperativeDTO the cooperativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cooperativeDTO,
     * or with status {@code 400 (Bad Request)} if the cooperativeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cooperativeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cooperativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cooperatives/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CooperativeDTO>> partialUpdateCooperative(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody CooperativeDTO cooperativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cooperative partially : {}, {}", id, cooperativeDTO);
        if (cooperativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cooperativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cooperativeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CooperativeDTO> result = cooperativeService.partialUpdate(cooperativeDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /cooperatives} : get all the cooperatives.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cooperatives in body.
     */
    @GetMapping("/cooperatives")
    public Mono<List<CooperativeDTO>> getAllCooperatives() {
        log.debug("REST request to get all Cooperatives");
        return cooperativeService.findAll().collectList();
    }

    /**
     * {@code GET  /cooperatives} : get all the cooperatives as a stream.
     * @return the {@link Flux} of cooperatives.
     */
    @GetMapping(value = "/cooperatives", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CooperativeDTO> getAllCooperativesAsStream() {
        log.debug("REST request to get all Cooperatives as a stream");
        return cooperativeService.findAll();
    }

    /**
     * {@code GET  /cooperatives/:id} : get the "id" cooperative.
     *
     * @param id the id of the cooperativeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cooperativeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cooperatives/{id}")
    public Mono<ResponseEntity<CooperativeDTO>> getCooperative(@PathVariable String id) {
        log.debug("REST request to get Cooperative : {}", id);
        Mono<CooperativeDTO> cooperativeDTO = cooperativeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cooperativeDTO);
    }

    /**
     * {@code DELETE  /cooperatives/:id} : delete the "id" cooperative.
     *
     * @param id the id of the cooperativeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cooperatives/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCooperative(@PathVariable String id) {
        log.debug("REST request to delete Cooperative : {}", id);
        return cooperativeService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
            );
    }
}
