package poitevie.coopcycle.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CooperativeMapperTest {

    private CooperativeMapper cooperativeMapper;

    @BeforeEach
    public void setUp() {
        cooperativeMapper = new CooperativeMapperImpl();
    }
}
