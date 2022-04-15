package poitevie.coopcycle.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import poitevie.coopcycle.web.rest.TestUtil;

class ShopTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shop.class);
        Shop shop1 = new Shop();
        shop1.setId(1L);
        Shop shop2 = new Shop();
        shop2.setId(shop1.getId());
        assertThat(shop1).isEqualTo(shop2);
        shop2.setId(2L);
        assertThat(shop1).isNotEqualTo(shop2);
        shop1.setId(null);
        assertThat(shop1).isNotEqualTo(shop2);
    }
}
