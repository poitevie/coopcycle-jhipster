package poitevie.coopcycle.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import poitevie.coopcycle.web.rest.TestUtil;

class CartDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartDTO.class);
        CartDTO cartDTO1 = new CartDTO();
        cartDTO1.setId(1L);
        CartDTO cartDTO2 = new CartDTO();
        assertThat(cartDTO1).isNotEqualTo(cartDTO2);
        cartDTO2.setId(cartDTO1.getId());
        assertThat(cartDTO1).isEqualTo(cartDTO2);
        cartDTO2.setId(2L);
        assertThat(cartDTO1).isNotEqualTo(cartDTO2);
        cartDTO1.setId(null);
        assertThat(cartDTO1).isNotEqualTo(cartDTO2);
    }
}
