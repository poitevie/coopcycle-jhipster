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
import poitevie.coopcycle.repository.ClientRepository;
import poitevie.coopcycle.service.ClientService;
import poitevie.coopcycle.service.dto.ClientDTO;
import poitevie.coopcycle.web.rest.errors.BadRequestAlertException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link poitevie.coopcycle.domain.Client}.
 */
@RestController
@RequestMapping("/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private static final String ENTITY_NAME = "client";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientService clientService;

    private final ClientRepository clientRepository;

    public ClientResource(ClientService clientService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    /**
     * {@code POST  /clients} : Create a new client.
     *
     * @param clientDTO the clientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientDTO, or with status {@code 400 (Bad Request)} if the client has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clients")
    public Mono<ResponseEntity<ClientDTO>> createClient(@Valid @RequestBody ClientDTO clientDTO) throws URISyntaxException {
        log.debug("REST request to save Client : {}", clientDTO);
        if (clientDTO.getId() != null) {
            throw new BadRequestAlertException("A new client cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return clientService
            .save(clientDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/clients/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /clients/:id} : Updates an existing client.
     *
     * @param id the id of the clientDTO to save.
     * @param clientDTO the clientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientDTO,
     * or with status {@code 400 (Bad Request)} if the clientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clients/{id}")
    public Mono<ResponseEntity<ClientDTO>> updateClient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClientDTO clientDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Client : {}, {}", id, clientDTO);
        if (clientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return clientRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return clientService
                    .update(clientDTO)
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
     * {@code PATCH  /clients/:id} : Partial updates given fields of an existing client, field will ignore if it is null
     *
     * @param id the id of the clientDTO to save.
     * @param clientDTO the clientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientDTO,
     * or with status {@code 400 (Bad Request)} if the clientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ClientDTO>> partialUpdateClient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClientDTO clientDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Client partially : {}, {}", id, clientDTO);
        if (clientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return clientRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ClientDTO> result = clientService.partialUpdate(clientDTO);

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
     * {@code GET  /clients} : get all the clients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clients in body.
     */
    @GetMapping("/clients")
    public Mono<List<ClientDTO>> getAllClients() {
        log.debug("REST request to get all Clients");
        return clientService.findAll().collectList();
    }

    /**
     * {@code GET  /clients} : get all the clients as a stream.
     * @return the {@link Flux} of clients.
     */
    @GetMapping(value = "/clients", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ClientDTO> getAllClientsAsStream() {
        log.debug("REST request to get all Clients as a stream");
        return clientService.findAll();
    }

    /**
     * {@code GET  /clients/:id} : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clients/{id}")
    public Mono<ResponseEntity<ClientDTO>> getClient(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        Mono<ClientDTO> clientDTO = clientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clientDTO);
    }

    /**
     * {@code DELETE  /clients/:id} : delete the "id" client.
     *
     * @param id the id of the clientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clients/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteClient(@PathVariable Long id) {
        log.debug("REST request to delete Client : {}", id);
        return clientService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
