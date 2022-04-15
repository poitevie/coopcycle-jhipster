package poitevie.coopcycle.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import poitevie.coopcycle.web.rest.TestUtil;

class ShopDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopDTO.class);
        ShopDTO shopDTO1 = new ShopDTO();
        shopDTO1.setId(1L);
        ShopDTO shopDTO2 = new ShopDTO();
        assertThat(shopDTO1).isNotEqualTo(shopDTO2);
        shopDTO2.setId(shopDTO1.getId());
        assertThat(shopDTO1).isEqualTo(shopDTO2);
        shopDTO2.setId(2L);
        assertThat(shopDTO1).isNotEqualTo(shopDTO2);
        shopDTO1.setId(null);
        assertThat(shopDTO1).isNotEqualTo(shopDTO2);
    }
}
