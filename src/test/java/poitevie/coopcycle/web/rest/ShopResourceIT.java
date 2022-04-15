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
import poitevie.coopcycle.domain.Shop;
import poitevie.coopcycle.repository.EntityManager;
import poitevie.coopcycle.repository.ShopRepository;
import poitevie.coopcycle.service.dto.ShopDTO;
import poitevie.coopcycle.service.mapper.ShopMapper;

/**
 * Integration tests for the {@link ShopResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ShopResourceIT {

    private static final String DEFAULT_ADDRESS_S = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_S = "BBBBBBBBBB";

    private static final String DEFAULT_MENU = "AAAAAAAAAA";
    private static final String UPDATED_MENU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shops";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Shop shop;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shop createEntity(EntityManager em) {
        Shop shop = new Shop().addressS(DEFAULT_ADDRESS_S).menu(DEFAULT_MENU);
        return shop;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shop createUpdatedEntity(EntityManager em) {
        Shop shop = new Shop().addressS(UPDATED_ADDRESS_S).menu(UPDATED_MENU);
        return shop;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Shop.class).block();
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
        shop = createEntity(em);
    }

    @Test
    void createShop() throws Exception {
        int databaseSizeBeforeCreate = shopRepository.findAll().collectList().block().size();
        // Create the Shop
        ShopDTO shopDTO = shopMapper.toDto(shop);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(shopDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeCreate + 1);
        Shop testShop = shopList.get(shopList.size() - 1);
        assertThat(testShop.getAddressS()).isEqualTo(DEFAULT_ADDRESS_S);
        assertThat(testShop.getMenu()).isEqualTo(DEFAULT_MENU);
    }

    @Test
    void createShopWithExistingId() throws Exception {
        // Create the Shop with an existing ID
        shop.setId(1L);
        ShopDTO shopDTO = shopMapper.toDto(shop);

        int databaseSizeBeforeCreate = shopRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(shopDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkAddressSIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopRepository.findAll().collectList().block().size();
        // set the field null
        shop.setAddressS(null);

        // Create the Shop, which fails.
        ShopDTO shopDTO = shopMapper.toDto(shop);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(shopDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllShopsAsStream() {
        // Initialize the database
        shopRepository.save(shop).block();

        List<Shop> shopList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ShopDTO.class)
            .getResponseBody()
            .map(shopMapper::toEntity)
            .filter(shop::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(shopList).isNotNull();
        assertThat(shopList).hasSize(1);
        Shop testShop = shopList.get(0);
        assertThat(testShop.getAddressS()).isEqualTo(DEFAULT_ADDRESS_S);
        assertThat(testShop.getMenu()).isEqualTo(DEFAULT_MENU);
    }

    @Test
    void getAllShops() {
        // Initialize the database
        shopRepository.save(shop).block();

        // Get all the shopList
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
            .value(hasItem(shop.getId().intValue()))
            .jsonPath("$.[*].addressS")
            .value(hasItem(DEFAULT_ADDRESS_S))
            .jsonPath("$.[*].menu")
            .value(hasItem(DEFAULT_MENU));
    }

    @Test
    void getShop() {
        // Initialize the database
        shopRepository.save(shop).block();

        // Get the shop
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, shop.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(shop.getId().intValue()))
            .jsonPath("$.addressS")
            .value(is(DEFAULT_ADDRESS_S))
            .jsonPath("$.menu")
            .value(is(DEFAULT_MENU));
    }

    @Test
    void getNonExistingShop() {
        // Get the shop
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewShop() throws Exception {
        // Initialize the database
        shopRepository.save(shop).block();

        int databaseSizeBeforeUpdate = shopRepository.findAll().collectList().block().size();

        // Update the shop
        Shop updatedShop = shopRepository.findById(shop.getId()).block();
        updatedShop.addressS(UPDATED_ADDRESS_S).menu(UPDATED_MENU);
        ShopDTO shopDTO = shopMapper.toDto(updatedShop);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, shopDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(shopDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
        Shop testShop = shopList.get(shopList.size() - 1);
        assertThat(testShop.getAddressS()).isEqualTo(UPDATED_ADDRESS_S);
        assertThat(testShop.getMenu()).isEqualTo(UPDATED_MENU);
    }

    @Test
    void putNonExistingShop() throws Exception {
        int databaseSizeBeforeUpdate = shopRepository.findAll().collectList().block().size();
        shop.setId(count.incrementAndGet());

        // Create the Shop
        ShopDTO shopDTO = shopMapper.toDto(shop);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, shopDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(shopDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchShop() throws Exception {
        int databaseSizeBeforeUpdate = shopRepository.findAll().collectList().block().size();
        shop.setId(count.incrementAndGet());

        // Create the Shop
        ShopDTO shopDTO = shopMapper.toDto(shop);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(shopDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamShop() throws Exception {
        int databaseSizeBeforeUpdate = shopRepository.findAll().collectList().block().size();
        shop.setId(count.incrementAndGet());

        // Create the Shop
        ShopDTO shopDTO = shopMapper.toDto(shop);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(shopDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateShopWithPatch() throws Exception {
        // Initialize the database
        shopRepository.save(shop).block();

        int databaseSizeBeforeUpdate = shopRepository.findAll().collectList().block().size();

        // Update the shop using partial update
        Shop partialUpdatedShop = new Shop();
        partialUpdatedShop.setId(shop.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedShop.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedShop))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
        Shop testShop = shopList.get(shopList.size() - 1);
        assertThat(testShop.getAddressS()).isEqualTo(DEFAULT_ADDRESS_S);
        assertThat(testShop.getMenu()).isEqualTo(DEFAULT_MENU);
    }

    @Test
    void fullUpdateShopWithPatch() throws Exception {
        // Initialize the database
        shopRepository.save(shop).block();

        int databaseSizeBeforeUpdate = shopRepository.findAll().collectList().block().size();

        // Update the shop using partial update
        Shop partialUpdatedShop = new Shop();
        partialUpdatedShop.setId(shop.getId());

        partialUpdatedShop.addressS(UPDATED_ADDRESS_S).menu(UPDATED_MENU);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedShop.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedShop))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
        Shop testShop = shopList.get(shopList.size() - 1);
        assertThat(testShop.getAddressS()).isEqualTo(UPDATED_ADDRESS_S);
        assertThat(testShop.getMenu()).isEqualTo(UPDATED_MENU);
    }

    @Test
    void patchNonExistingShop() throws Exception {
        int databaseSizeBeforeUpdate = shopRepository.findAll().collectList().block().size();
        shop.setId(count.incrementAndGet());

        // Create the Shop
        ShopDTO shopDTO = shopMapper.toDto(shop);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, shopDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(shopDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchShop() throws Exception {
        int databaseSizeBeforeUpdate = shopRepository.findAll().collectList().block().size();
        shop.setId(count.incrementAndGet());

        // Create the Shop
        ShopDTO shopDTO = shopMapper.toDto(shop);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(shopDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamShop() throws Exception {
        int databaseSizeBeforeUpdate = shopRepository.findAll().collectList().block().size();
        shop.setId(count.incrementAndGet());

        // Create the Shop
        ShopDTO shopDTO = shopMapper.toDto(shop);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(shopDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteShop() {
        // Initialize the database
        shopRepository.save(shop).block();

        int databaseSizeBeforeDelete = shopRepository.findAll().collectList().block().size();

        // Delete the shop
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, shop.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Shop> shopList = shopRepository.findAll().collectList().block();
        assertThat(shopList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
