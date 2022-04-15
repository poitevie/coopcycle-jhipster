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
import poitevie.coopcycle.domain.Client;
import poitevie.coopcycle.repository.ClientRepository;
import poitevie.coopcycle.repository.EntityManager;
import poitevie.coopcycle.service.dto.ClientDTO;
import poitevie.coopcycle.service.mapper.ClientMapper;

/**
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ClientResourceIT {

    private static final String DEFAULT_ID_C = "AAAAAAAAAA";
    private static final String UPDATED_ID_C = "BBBBBBBBBB";

    private static final String DEFAULT_FIRSTNAME_C = "Onhekne";
    private static final String UPDATED_FIRSTNAME_C = "Twsvw";

    private static final String DEFAULT_LASTNAME_C = "Hr";
    private static final String UPDATED_LASTNAME_C = "Jro";

    private static final String DEFAULT_EMAIL_C = "A@P5l7Og\\QtnNkk";
    private static final String UPDATED_EMAIL_C = "YMYz1.@012n\\FDHu";

    private static final String DEFAULT_PHONE_C = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_C = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_C = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_C = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Client client;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
            .idC(DEFAULT_ID_C)
            .firstnameC(DEFAULT_FIRSTNAME_C)
            .lastnameC(DEFAULT_LASTNAME_C)
            .emailC(DEFAULT_EMAIL_C)
            .phoneC(DEFAULT_PHONE_C)
            .addressC(DEFAULT_ADDRESS_C);
        return client;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity(EntityManager em) {
        Client client = new Client()
            .idC(UPDATED_ID_C)
            .firstnameC(UPDATED_FIRSTNAME_C)
            .lastnameC(UPDATED_LASTNAME_C)
            .emailC(UPDATED_EMAIL_C)
            .phoneC(UPDATED_PHONE_C)
            .addressC(UPDATED_ADDRESS_C);
        return client;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Client.class).block();
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
        client = createEntity(em);
    }

    @Test
    void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().collectList().block().size();
        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getIdC()).isEqualTo(DEFAULT_ID_C);
        assertThat(testClient.getFirstnameC()).isEqualTo(DEFAULT_FIRSTNAME_C);
        assertThat(testClient.getLastnameC()).isEqualTo(DEFAULT_LASTNAME_C);
        assertThat(testClient.getEmailC()).isEqualTo(DEFAULT_EMAIL_C);
        assertThat(testClient.getPhoneC()).isEqualTo(DEFAULT_PHONE_C);
        assertThat(testClient.getAddressC()).isEqualTo(DEFAULT_ADDRESS_C);
    }

    @Test
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        client.setId(1L);
        ClientDTO clientDTO = clientMapper.toDto(client);

        int databaseSizeBeforeCreate = clientRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdCIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().collectList().block().size();
        // set the field null
        client.setIdC(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFirstnameCIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().collectList().block().size();
        // set the field null
        client.setFirstnameC(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLastnameCIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().collectList().block().size();
        // set the field null
        client.setLastnameC(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailCIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().collectList().block().size();
        // set the field null
        client.setEmailC(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAddressCIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().collectList().block().size();
        // set the field null
        client.setAddressC(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllClientsAsStream() {
        // Initialize the database
        clientRepository.save(client).block();

        List<Client> clientList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ClientDTO.class)
            .getResponseBody()
            .map(clientMapper::toEntity)
            .filter(client::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(clientList).isNotNull();
        assertThat(clientList).hasSize(1);
        Client testClient = clientList.get(0);
        assertThat(testClient.getIdC()).isEqualTo(DEFAULT_ID_C);
        assertThat(testClient.getFirstnameC()).isEqualTo(DEFAULT_FIRSTNAME_C);
        assertThat(testClient.getLastnameC()).isEqualTo(DEFAULT_LASTNAME_C);
        assertThat(testClient.getEmailC()).isEqualTo(DEFAULT_EMAIL_C);
        assertThat(testClient.getPhoneC()).isEqualTo(DEFAULT_PHONE_C);
        assertThat(testClient.getAddressC()).isEqualTo(DEFAULT_ADDRESS_C);
    }

    @Test
    void getAllClients() {
        // Initialize the database
        clientRepository.save(client).block();

        // Get all the clientList
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
            .value(hasItem(client.getId().intValue()))
            .jsonPath("$.[*].idC")
            .value(hasItem(DEFAULT_ID_C))
            .jsonPath("$.[*].firstnameC")
            .value(hasItem(DEFAULT_FIRSTNAME_C))
            .jsonPath("$.[*].lastnameC")
            .value(hasItem(DEFAULT_LASTNAME_C))
            .jsonPath("$.[*].emailC")
            .value(hasItem(DEFAULT_EMAIL_C))
            .jsonPath("$.[*].phoneC")
            .value(hasItem(DEFAULT_PHONE_C))
            .jsonPath("$.[*].addressC")
            .value(hasItem(DEFAULT_ADDRESS_C));
    }

    @Test
    void getClient() {
        // Initialize the database
        clientRepository.save(client).block();

        // Get the client
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, client.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(client.getId().intValue()))
            .jsonPath("$.idC")
            .value(is(DEFAULT_ID_C))
            .jsonPath("$.firstnameC")
            .value(is(DEFAULT_FIRSTNAME_C))
            .jsonPath("$.lastnameC")
            .value(is(DEFAULT_LASTNAME_C))
            .jsonPath("$.emailC")
            .value(is(DEFAULT_EMAIL_C))
            .jsonPath("$.phoneC")
            .value(is(DEFAULT_PHONE_C))
            .jsonPath("$.addressC")
            .value(is(DEFAULT_ADDRESS_C));
    }

    @Test
    void getNonExistingClient() {
        // Get the client
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewClient() throws Exception {
        // Initialize the database
        clientRepository.save(client).block();

        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).block();
        updatedClient
            .idC(UPDATED_ID_C)
            .firstnameC(UPDATED_FIRSTNAME_C)
            .lastnameC(UPDATED_LASTNAME_C)
            .emailC(UPDATED_EMAIL_C)
            .phoneC(UPDATED_PHONE_C)
            .addressC(UPDATED_ADDRESS_C);
        ClientDTO clientDTO = clientMapper.toDto(updatedClient);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, clientDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getIdC()).isEqualTo(UPDATED_ID_C);
        assertThat(testClient.getFirstnameC()).isEqualTo(UPDATED_FIRSTNAME_C);
        assertThat(testClient.getLastnameC()).isEqualTo(UPDATED_LASTNAME_C);
        assertThat(testClient.getEmailC()).isEqualTo(UPDATED_EMAIL_C);
        assertThat(testClient.getPhoneC()).isEqualTo(UPDATED_PHONE_C);
        assertThat(testClient.getAddressC()).isEqualTo(UPDATED_ADDRESS_C);
    }

    @Test
    void putNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, clientDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.save(client).block();

        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient.idC(UPDATED_ID_C).emailC(UPDATED_EMAIL_C).phoneC(UPDATED_PHONE_C).addressC(UPDATED_ADDRESS_C);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClient.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getIdC()).isEqualTo(UPDATED_ID_C);
        assertThat(testClient.getFirstnameC()).isEqualTo(DEFAULT_FIRSTNAME_C);
        assertThat(testClient.getLastnameC()).isEqualTo(DEFAULT_LASTNAME_C);
        assertThat(testClient.getEmailC()).isEqualTo(UPDATED_EMAIL_C);
        assertThat(testClient.getPhoneC()).isEqualTo(UPDATED_PHONE_C);
        assertThat(testClient.getAddressC()).isEqualTo(UPDATED_ADDRESS_C);
    }

    @Test
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.save(client).block();

        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .idC(UPDATED_ID_C)
            .firstnameC(UPDATED_FIRSTNAME_C)
            .lastnameC(UPDATED_LASTNAME_C)
            .emailC(UPDATED_EMAIL_C)
            .phoneC(UPDATED_PHONE_C)
            .addressC(UPDATED_ADDRESS_C);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClient.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getIdC()).isEqualTo(UPDATED_ID_C);
        assertThat(testClient.getFirstnameC()).isEqualTo(UPDATED_FIRSTNAME_C);
        assertThat(testClient.getLastnameC()).isEqualTo(UPDATED_LASTNAME_C);
        assertThat(testClient.getEmailC()).isEqualTo(UPDATED_EMAIL_C);
        assertThat(testClient.getPhoneC()).isEqualTo(UPDATED_PHONE_C);
        assertThat(testClient.getAddressC()).isEqualTo(UPDATED_ADDRESS_C);
    }

    @Test
    void patchNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, clientDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteClient() {
        // Initialize the database
        clientRepository.save(client).block();

        int databaseSizeBeforeDelete = clientRepository.findAll().collectList().block().size();

        // Delete the client
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, client.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
