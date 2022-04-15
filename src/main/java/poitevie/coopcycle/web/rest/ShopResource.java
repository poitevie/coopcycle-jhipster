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
import poitevie.coopcycle.repository.ShopRepository;
import poitevie.coopcycle.service.ShopService;
import poitevie.coopcycle.service.dto.ShopDTO;
import poitevie.coopcycle.web.rest.errors.BadRequestAlertException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link poitevie.coopcycle.domain.Shop}.
 */
@RestController
@RequestMapping("/api")
public class ShopResource {

    private final Logger log = LoggerFactory.getLogger(ShopResource.class);

    private static final String ENTITY_NAME = "shop";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopService shopService;

    private final ShopRepository shopRepository;

    public ShopResource(ShopService shopService, ShopRepository shopRepository) {
        this.shopService = shopService;
        this.shopRepository = shopRepository;
    }

    /**
     * {@code POST  /shops} : Create a new shop.
     *
     * @param shopDTO the shopDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopDTO, or with status {@code 400 (Bad Request)} if the shop has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shops")
    public Mono<ResponseEntity<ShopDTO>> createShop(@Valid @RequestBody ShopDTO shopDTO) throws URISyntaxException {
        log.debug("REST request to save Shop : {}", shopDTO);
        if (shopDTO.getId() != null) {
            throw new BadRequestAlertException("A new shop cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return shopService
            .save(shopDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/shops/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /shops/:id} : Updates an existing shop.
     *
     * @param id the id of the shopDTO to save.
     * @param shopDTO the shopDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopDTO,
     * or with status {@code 400 (Bad Request)} if the shopDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shops/{id}")
    public Mono<ResponseEntity<ShopDTO>> updateShop(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShopDTO shopDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Shop : {}, {}", id, shopDTO);
        if (shopDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shopDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return shopRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return shopService
                    .update(shopDTO)
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
     * {@code PATCH  /shops/:id} : Partial updates given fields of an existing shop, field will ignore if it is null
     *
     * @param id the id of the shopDTO to save.
     * @param shopDTO the shopDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopDTO,
     * or with status {@code 400 (Bad Request)} if the shopDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shopDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shopDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shops/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ShopDTO>> partialUpdateShop(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShopDTO shopDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Shop partially : {}, {}", id, shopDTO);
        if (shopDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shopDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return shopRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ShopDTO> result = shopService.partialUpdate(shopDTO);

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
     * {@code GET  /shops} : get all the shops.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shops in body.
     */
    @GetMapping("/shops")
    public Mono<List<ShopDTO>> getAllShops() {
        log.debug("REST request to get all Shops");
        return shopService.findAll().collectList();
    }

    /**
     * {@code GET  /shops} : get all the shops as a stream.
     * @return the {@link Flux} of shops.
     */
    @GetMapping(value = "/shops", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ShopDTO> getAllShopsAsStream() {
        log.debug("REST request to get all Shops as a stream");
        return shopService.findAll();
    }

    /**
     * {@code GET  /shops/:id} : get the "id" shop.
     *
     * @param id the id of the shopDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shops/{id}")
    public Mono<ResponseEntity<ShopDTO>> getShop(@PathVariable Long id) {
        log.debug("REST request to get Shop : {}", id);
        Mono<ShopDTO> shopDTO = shopService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shopDTO);
    }

    /**
     * {@code DELETE  /shops/:id} : delete the "id" shop.
     *
     * @param id the id of the shopDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shops/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteShop(@PathVariable Long id) {
        log.debug("REST request to delete Shop : {}", id);
        return shopService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
