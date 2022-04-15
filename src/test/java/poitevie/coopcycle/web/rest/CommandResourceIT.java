package poitevie.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import poitevie.coopcycle.IntegrationTest;
import poitevie.coopcycle.domain.Command;
import poitevie.coopcycle.repository.CommandRepository;
import poitevie.coopcycle.repository.EntityManager;
import poitevie.coopcycle.service.dto.CommandDTO;
import poitevie.coopcycle.service.mapper.CommandMapper;

/**
 * Integration tests for the {@link CommandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CommandResourceIT {

    private static final String DEFAULT_ADDRESS_C = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_C = "BBBBBBBBBB";

    private static final Float DEFAULT_DATE_C = 1F;
    private static final Float UPDATED_DATE_C = 2F;

    private static final String ENTITY_API_URL = "/api/commands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private CommandMapper commandMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Command command;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Command createEntity(EntityManager em) {
        Command command = new Command().addressC(DEFAULT_ADDRESS_C).dateC(DEFAULT_DATE_C);
        return command;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Command createUpdatedEntity(EntityManager em) {
        Command command = new Command().addressC(UPDATED_ADDRESS_C).dateC(UPDATED_DATE_C);
        return command;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Command.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        command = createEntity(em);
    }

    @Test
    void createCommand() throws Exception {
        int databaseSizeBeforeCreate = commandRepository.findAll().collectList().block().size();
        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeCreate + 1);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getAddressC()).isEqualTo(DEFAULT_ADDRESS_C);
        assertThat(testCommand.getDateC()).isEqualTo(DEFAULT_DATE_C);
    }

    @Test
    void createCommandWithExistingId() throws Exception {
        // Create the Command with an existing ID
        command.setId(1L);
        CommandDTO commandDTO = commandMapper.toDto(command);

        int databaseSizeBeforeCreate = commandRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkAddressCIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandRepository.findAll().collectList().block().size();
        // set the field null
        command.setAddressC(null);

        // Create the Command, which fails.
        CommandDTO commandDTO = commandMapper.toDto(command);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDateCIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandRepository.findAll().collectList().block().size();
        // set the field null
        command.setDateC(null);

        // Create the Command, which fails.
        CommandDTO commandDTO = commandMapper.toDto(command);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCommandsAsStream() {
        // Initialize the database
        commandRepository.save(command).block();

        List<Command> commandList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CommandDTO.class)
            .getResponseBody()
            .map(commandMapper::toEntity)
            .filter(command::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(commandList).isNotNull();
        assertThat(commandList).hasSize(1);
        Command testCommand = commandList.get(0);
        assertThat(testCommand.getAddressC()).isEqualTo(DEFAULT_ADDRESS_C);
        assertThat(testCommand.getDateC()).isEqualTo(DEFAULT_DATE_C);
    }

    @Test
    void getAllCommands() {
        // Initialize the database
        commandRepository.save(command).block();

        // Get all the commandList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(command.getId().intValue()))
            .jsonPath("$.[*].addressC")
            .value(hasItem(DEFAULT_ADDRESS_C))
            .jsonPath("$.[*].dateC")
            .value(hasItem(DEFAULT_DATE_C.doubleValue()));
    }

    @Test
    void getCommand() {
        // Initialize the database
        commandRepository.save(command).block();

        // Get the command
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, command.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(command.getId().intValue()))
            .jsonPath("$.addressC")
            .value(is(DEFAULT_ADDRESS_C))
            .jsonPath("$.dateC")
            .value(is(DEFAULT_DATE_C.doubleValue()));
    }

    @Test
    void getNonExistingCommand() {
        // Get the command
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCommand() throws Exception {
        // Initialize the database
        commandRepository.save(command).block();

        int databaseSizeBeforeUpdate = commandRepository.findAll().collectList().block().size();

        // Update the command
        Command updatedCommand = commandRepository.findById(command.getId()).block();
        updatedCommand.addressC(UPDATED_ADDRESS_C).dateC(UPDATED_DATE_C);
        CommandDTO commandDTO = commandMapper.toDto(updatedCommand);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commandDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getAddressC()).isEqualTo(UPDATED_ADDRESS_C);
        assertThat(testCommand.getDateC()).isEqualTo(UPDATED_DATE_C);
    }

    @Test
    void putNonExistingCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().collectList().block().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commandDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().collectList().block().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().collectList().block().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCommandWithPatch() throws Exception {
        // Initialize the database
        commandRepository.save(command).block();

        int databaseSizeBeforeUpdate = commandRepository.findAll().collectList().block().size();

        // Update the command using partial update
        Command partialUpdatedCommand = new Command();
        partialUpdatedCommand.setId(command.getId());

        partialUpdatedCommand.addressC(UPDATED_ADDRESS_C).dateC(UPDATED_DATE_C);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommand.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommand))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getAddressC()).isEqualTo(UPDATED_ADDRESS_C);
        assertThat(testCommand.getDateC()).isEqualTo(UPDATED_DATE_C);
    }

    @Test
    void fullUpdateCommandWithPatch() throws Exception {
        // Initialize the database
        commandRepository.save(command).block();

        int databaseSizeBeforeUpdate = commandRepository.findAll().collectList().block().size();

        // Update the command using partial update
        Command partialUpdatedCommand = new Command();
        partialUpdatedCommand.setId(command.getId());

        partialUpdatedCommand.addressC(UPDATED_ADDRESS_C).dateC(UPDATED_DATE_C);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommand.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommand))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getAddressC()).isEqualTo(UPDATED_ADDRESS_C);
        assertThat(testCommand.getDateC()).isEqualTo(UPDATED_DATE_C);
    }

    @Test
    void patchNonExistingCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().collectList().block().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, commandDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().collectList().block().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().collectList().block().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCommand() {
        // Initialize the database
        commandRepository.save(command).block();

        int databaseSizeBeforeDelete = commandRepository.findAll().collectList().block().size();

        // Delete the command
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, command.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Command> commandList = commandRepository.findAll().collectList().block();
        assertThat(commandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
