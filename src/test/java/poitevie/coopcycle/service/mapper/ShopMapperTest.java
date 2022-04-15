package poitevie.coopcycle.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShopMapperTest {

    private ShopMapper shopMapper;

    @BeforeEach
    public void setUp() {
        shopMapper = new ShopMapperImpl();
    }
}
