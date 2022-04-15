package poitevie.coopcycle.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poitevie.coopcycle.domain.Client;
import poitevie.coopcycle.repository.ClientRepository;
import poitevie.coopcycle.service.dto.ClientDTO;
import poitevie.coopcycle.service.mapper.ClientMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Client}.
 */
@Service
@Transactional
public class ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    /**
     * Save a client.
     *
     * @param clientDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ClientDTO> save(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO);
        return clientRepository.save(clientMapper.toEntity(clientDTO)).map(clientMapper::toDto);
    }

    /**
     * Update a client.
     *
     * @param clientDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ClientDTO> update(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO);
        return clientRepository.save(clientMapper.toEntity(clientDTO)).map(clientMapper::toDto);
    }

    /**
     * Partially update a client.
     *
     * @param clientDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ClientDTO> partialUpdate(ClientDTO clientDTO) {
        log.debug("Request to partially update Client : {}", clientDTO);

        return clientRepository
            .findById(clientDTO.getId())
            .map(existingClient -> {
                clientMapper.partialUpdate(existingClient, clientDTO);

                return existingClient;
            })
            .flatMap(clientRepository::save)
            .map(clientMapper::toDto);
    }

    /**
     * Get all the clients.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ClientDTO> findAll() {
        log.debug("Request to get all Clients");
        return clientRepository.findAll().map(clientMapper::toDto);
    }

    /**
     * Returns the number of clients available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return clientRepository.count();
    }

    /**
     * Get one client by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ClientDTO> findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findById(id).map(clientMapper::toDto);
    }

    /**
     * Delete the client by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        return clientRepository.deleteById(id);
    }
}
