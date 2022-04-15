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
import poitevie.coopcycle.repository.CommandRepository;
import poitevie.coopcycle.service.CommandService;
import poitevie.coopcycle.service.dto.CommandDTO;
import poitevie.coopcycle.web.rest.errors.BadRequestAlertException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link poitevie.coopcycle.domain.Command}.
 */
@RestController
@RequestMapping("/api")
public class CommandResource {

    private final Logger log = LoggerFactory.getLogger(CommandResource.class);

    private static final String ENTITY_NAME = "command";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommandService commandService;

    private final CommandRepository commandRepository;

    public CommandResource(CommandService commandService, CommandRepository commandRepository) {
        this.commandService = commandService;
        this.commandRepository = commandRepository;
    }

    /**
     * {@code POST  /commands} : Create a new command.
     *
     * @param commandDTO the commandDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commandDTO, or with status {@code 400 (Bad Request)} if the command has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commands")
    public Mono<ResponseEntity<CommandDTO>> createCommand(@Valid @RequestBody CommandDTO commandDTO) throws URISyntaxException {
        log.debug("REST request to save Command : {}", commandDTO);
        if (commandDTO.getId() != null) {
            throw new BadRequestAlertException("A new command cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return commandService
            .save(commandDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/commands/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /commands/:id} : Updates an existing command.
     *
     * @param id the id of the commandDTO to save.
     * @param commandDTO the commandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commandDTO,
     * or with status {@code 400 (Bad Request)} if the commandDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commands/{id}")
    public Mono<ResponseEntity<CommandDTO>> updateCommand(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommandDTO commandDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Command : {}, {}", id, commandDTO);
        if (commandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return commandRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return commandService
                    .update(commandDTO)
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
     * {@code PATCH  /commands/:id} : Partial updates given fields of an existing command, field will ignore if it is null
     *
     * @param id the id of the commandDTO to save.
     * @param commandDTO the commandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commandDTO,
     * or with status {@code 400 (Bad Request)} if the commandDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commandDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commands/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CommandDTO>> partialUpdateCommand(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommandDTO commandDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Command partially : {}, {}", id, commandDTO);
        if (commandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return commandRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CommandDTO> result = commandService.partialUpdate(commandDTO);

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
     * {@code GET  /commands} : get all the commands.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commands in body.
     */
    @GetMapping("/commands")
    public Mono<List<CommandDTO>> getAllCommands(@RequestParam(required = false) String filter) {
        if ("cart-is-null".equals(filter)) {
            log.debug("REST request to get all Commands where cart is null");
            return commandService.findAllWhereCartIsNull().collectList();
        }
        log.debug("REST request to get all Commands");
        return commandService.findAll().collectList();
    }

    /**
     * {@code GET  /commands} : get all the commands as a stream.
     * @return the {@link Flux} of commands.
     */
    @GetMapping(value = "/commands", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CommandDTO> getAllCommandsAsStream() {
        log.debug("REST request to get all Commands as a stream");
        return commandService.findAll();
    }

    /**
     * {@code GET  /commands/:id} : get the "id" command.
     *
     * @param id the id of the commandDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commandDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commands/{id}")
    public Mono<ResponseEntity<CommandDTO>> getCommand(@PathVariable Long id) {
        log.debug("REST request to get Command : {}", id);
        Mono<CommandDTO> commandDTO = commandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commandDTO);
    }

    /**
     * {@code DELETE  /commands/:id} : delete the "id" command.
     *
     * @param id the id of the commandDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commands/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCommand(@PathVariable Long id) {
        log.debug("REST request to delete Command : {}", id);
        return commandService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
