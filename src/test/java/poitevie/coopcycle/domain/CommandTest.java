package poitevie.coopcycle.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import poitevie.coopcycle.web.rest.TestUtil;

class CommandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Command.class);
        Command command1 = new Command();
        command1.setId(1L);
        Command command2 = new Command();
        command2.setId(command1.getId());
        assertThat(command1).isEqualTo(command2);
        command2.setId(2L);
        assertThat(command1).isNotEqualTo(command2);
        command1.setId(null);
        assertThat(command1).isNotEqualTo(command2);
    }
}
