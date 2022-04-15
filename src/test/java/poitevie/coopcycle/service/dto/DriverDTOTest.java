package poitevie.coopcycle.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import poitevie.coopcycle.web.rest.TestUtil;

class DriverDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DriverDTO.class);
        DriverDTO driverDTO1 = new DriverDTO();
        driverDTO1.setId(1L);
        DriverDTO driverDTO2 = new DriverDTO();
        assertThat(driverDTO1).isNotEqualTo(driverDTO2);
        driverDTO2.setId(driverDTO1.getId());
        assertThat(driverDTO1).isEqualTo(driverDTO2);
        driverDTO2.setId(2L);
        assertThat(driverDTO1).isNotEqualTo(driverDTO2);
        driverDTO1.setId(null);
        assertThat(driverDTO1).isNotEqualTo(driverDTO2);
    }
}
