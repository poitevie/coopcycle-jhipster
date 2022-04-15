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
import poitevie.coopcycle.domain.Driver;
import poitevie.coopcycle.repository.DriverRepository;
import poitevie.coopcycle.repository.EntityManager;
import poitevie.coopcycle.service.dto.DriverDTO;
import poitevie.coopcycle.service.mapper.DriverMapper;

/**
 * Integration tests for the {@link DriverResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DriverResourceIT {

    private static final String DEFAULT_FIRSTNAME_D = "Sorvfs";
    private static final String UPDATED_FIRSTNAME_D = "Jvk";

    private static final String DEFAULT_LASTNAME_D = "Qq";
    private static final String UPDATED_LASTNAME_D = "Yla";

    private static final String DEFAULT_PHONE_D = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_D = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/drivers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverMapper driverMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Driver driver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Driver createEntity(EntityManager em) {
        Driver driver = new Driver().firstnameD(DEFAULT_FIRSTNAME_D).lastnameD(DEFAULT_LASTNAME_D).phoneD(DEFAULT_PHONE_D);
        return driver;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Driver createUpdatedEntity(EntityManager em) {
        Driver driver = new Driver().firstnameD(UPDATED_FIRSTNAME_D).lastnameD(UPDATED_LASTNAME_D).phoneD(UPDATED_PHONE_D);
        return driver;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Driver.class).block();
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
        driver = createEntity(em);
    }

    @Test
    void createDriver() throws Exception {
        int databaseSizeBeforeCreate = driverRepository.findAll().collectList().block().size();
        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(driverDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeCreate + 1);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getFirstnameD()).isEqualTo(DEFAULT_FIRSTNAME_D);
        assertThat(testDriver.getLastnameD()).isEqualTo(DEFAULT_LASTNAME_D);
        assertThat(testDriver.getPhoneD()).isEqualTo(DEFAULT_PHONE_D);
    }

    @Test
    void createDriverWithExistingId() throws Exception {
        // Create the Driver with an existing ID
        driver.setId(1L);
        DriverDTO driverDTO = driverMapper.toDto(driver);

        int databaseSizeBeforeCreate = driverRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(driverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkFirstnameDIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverRepository.findAll().collectList().block().size();
        // set the field null
        driver.setFirstnameD(null);

        // Create the Driver, which fails.
        DriverDTO driverDTO = driverMapper.toDto(driver);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(driverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLastnameDIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverRepository.findAll().collectList().block().size();
        // set the field null
        driver.setLastnameD(null);

        // Create the Driver, which fails.
        DriverDTO driverDTO = driverMapper.toDto(driver);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(driverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDriversAsStream() {
        // Initialize the database
        driverRepository.save(driver).block();

        List<Driver> driverList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DriverDTO.class)
            .getResponseBody()
            .map(driverMapper::toEntity)
            .filter(driver::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(driverList).isNotNull();
        assertThat(driverList).hasSize(1);
        Driver testDriver = driverList.get(0);
        assertThat(testDriver.getFirstnameD()).isEqualTo(DEFAULT_FIRSTNAME_D);
        assertThat(testDriver.getLastnameD()).isEqualTo(DEFAULT_LASTNAME_D);
        assertThat(testDriver.getPhoneD()).isEqualTo(DEFAULT_PHONE_D);
    }

    @Test
    void getAllDrivers() {
        // Initialize the database
        driverRepository.save(driver).block();

        // Get all the driverList
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
            .value(hasItem(driver.getId().intValue()))
            .jsonPath("$.[*].firstnameD")
            .value(hasItem(DEFAULT_FIRSTNAME_D))
            .jsonPath("$.[*].lastnameD")
            .value(hasItem(DEFAULT_LASTNAME_D))
            .jsonPath("$.[*].phoneD")
            .value(hasItem(DEFAULT_PHONE_D));
    }

    @Test
    void getDriver() {
        // Initialize the database
        driverRepository.save(driver).block();

        // Get the driver
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, driver.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(driver.getId().intValue()))
            .jsonPath("$.firstnameD")
            .value(is(DEFAULT_FIRSTNAME_D))
            .jsonPath("$.lastnameD")
            .value(is(DEFAULT_LASTNAME_D))
            .jsonPath("$.phoneD")
            .value(is(DEFAULT_PHONE_D));
    }

    @Test
    void getNonExistingDriver() {
        // Get the driver
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDriver() throws Exception {
        // Initialize the database
        driverRepository.save(driver).block();

        int databaseSizeBeforeUpdate = driverRepository.findAll().collectList().block().size();

        // Update the driver
        Driver updatedDriver = driverRepository.findById(driver.getId()).block();
        updatedDriver.firstnameD(UPDATED_FIRSTNAME_D).lastnameD(UPDATED_LASTNAME_D).phoneD(UPDATED_PHONE_D);
        DriverDTO driverDTO = driverMapper.toDto(updatedDriver);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, driverDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(driverDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getFirstnameD()).isEqualTo(UPDATED_FIRSTNAME_D);
        assertThat(testDriver.getLastnameD()).isEqualTo(UPDATED_LASTNAME_D);
        assertThat(testDriver.getPhoneD()).isEqualTo(UPDATED_PHONE_D);
    }

    @Test
    void putNonExistingDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().collectList().block().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, driverDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(driverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().collectList().block().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(driverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().collectList().block().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(driverDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDriverWithPatch() throws Exception {
        // Initialize the database
        driverRepository.save(driver).block();

        int databaseSizeBeforeUpdate = driverRepository.findAll().collectList().block().size();

        // Update the driver using partial update
        Driver partialUpdatedDriver = new Driver();
        partialUpdatedDriver.setId(driver.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDriver.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDriver))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getFirstnameD()).isEqualTo(DEFAULT_FIRSTNAME_D);
        assertThat(testDriver.getLastnameD()).isEqualTo(DEFAULT_LASTNAME_D);
        assertThat(testDriver.getPhoneD()).isEqualTo(DEFAULT_PHONE_D);
    }

    @Test
    void fullUpdateDriverWithPatch() throws Exception {
        // Initialize the database
        driverRepository.save(driver).block();

        int databaseSizeBeforeUpdate = driverRepository.findAll().collectList().block().size();

        // Update the driver using partial update
        Driver partialUpdatedDriver = new Driver();
        partialUpdatedDriver.setId(driver.getId());

        partialUpdatedDriver.firstnameD(UPDATED_FIRSTNAME_D).lastnameD(UPDATED_LASTNAME_D).phoneD(UPDATED_PHONE_D);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDriver.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDriver))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getFirstnameD()).isEqualTo(UPDATED_FIRSTNAME_D);
        assertThat(testDriver.getLastnameD()).isEqualTo(UPDATED_LASTNAME_D);
        assertThat(testDriver.getPhoneD()).isEqualTo(UPDATED_PHONE_D);
    }

    @Test
    void patchNonExistingDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().collectList().block().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, driverDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(driverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().collectList().block().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(driverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().collectList().block().size();
        driver.setId(count.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(driverDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDriver() {
        // Initialize the database
        driverRepository.save(driver).block();

        int databaseSizeBeforeDelete = driverRepository.findAll().collectList().block().size();

        // Delete the driver
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, driver.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Driver> driverList = driverRepository.findAll().collectList().block();
        assertThat(driverList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
