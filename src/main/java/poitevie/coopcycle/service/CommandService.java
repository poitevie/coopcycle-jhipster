package poitevie.coopcycle.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poitevie.coopcycle.domain.Command;
import poitevie.coopcycle.repository.CommandRepository;
import poitevie.coopcycle.service.dto.CommandDTO;
import poitevie.coopcycle.service.mapper.CommandMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Command}.
 */
@Service
@Transactional
public class CommandService {

    private final Logger log = LoggerFactory.getLogger(CommandService.class);

    private final CommandRepository commandRepository;

    private final CommandMapper commandMapper;

    public CommandService(CommandRepository commandRepository, CommandMapper commandMapper) {
        this.commandRepository = commandRepository;
        this.commandMapper = commandMapper;
    }

    /**
     * Save a command.
     *
     * @param commandDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CommandDTO> save(CommandDTO commandDTO) {
        log.debug("Request to save Command : {}", commandDTO);
        return commandRepository.save(commandMapper.toEntity(commandDTO)).map(commandMapper::toDto);
    }

    /**
     * Update a command.
     *
     * @param commandDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CommandDTO> update(CommandDTO commandDTO) {
        log.debug("Request to save Command : {}", commandDTO);
        return commandRepository.save(commandMapper.toEntity(commandDTO)).map(commandMapper::toDto);
    }

    /**
     * Partially update a command.
     *
     * @param commandDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CommandDTO> partialUpdate(CommandDTO commandDTO) {
        log.debug("Request to partially update Command : {}", commandDTO);

        return commandRepository
            .findById(commandDTO.getId())
            .map(existingCommand -> {
                commandMapper.partialUpdate(existingCommand, commandDTO);

                return existingCommand;
            })
            .flatMap(commandRepository::save)
            .map(commandMapper::toDto);
    }

    /**
     * Get all the commands.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CommandDTO> findAll() {
        log.debug("Request to get all Commands");
        return commandRepository.findAll().map(commandMapper::toDto);
    }

    /**
     *  Get all the commands where Cart is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CommandDTO> findAllWhereCartIsNull() {
        log.debug("Request to get all commands where Cart is null");
        return commandRepository.findAllWhereCartIsNull().map(commandMapper::toDto);
    }

    /**
     * Returns the number of commands available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return commandRepository.count();
    }

    /**
     * Get one command by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CommandDTO> findOne(Long id) {
        log.debug("Request to get Command : {}", id);
        return commandRepository.findById(id).map(commandMapper::toDto);
    }

    /**
     * Delete the command by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Command : {}", id);
        return commandRepository.deleteById(id);
    }
}
