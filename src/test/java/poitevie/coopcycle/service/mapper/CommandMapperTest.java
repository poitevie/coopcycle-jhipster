package poitevie.coopcycle.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandMapperTest {

    private CommandMapper commandMapper;

    @BeforeEach
    public void setUp() {
        commandMapper = new CommandMapperImpl();
    }
}
