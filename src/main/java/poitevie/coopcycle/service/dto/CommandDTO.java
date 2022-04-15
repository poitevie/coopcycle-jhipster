package poitevie.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link poitevie.coopcycle.domain.Command} entity.
 */
public class CommandDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String addressC;

    @NotNull(message = "must not be null")
    private Float dateC;

    private ClientDTO client;

    private DriverDTO driver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressC() {
        return addressC;
    }

    public void setAddressC(String addressC) {
        this.addressC = addressC;
    }

    public Float getDateC() {
        return dateC;
    }

    public void setDateC(Float dateC) {
        this.dateC = dateC;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public DriverDTO getDriver() {
        return driver;
    }

    public void setDriver(DriverDTO driver) {
        this.driver = driver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandDTO)) {
            return false;
        }

        CommandDTO commandDTO = (CommandDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commandDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandDTO{" +
            "id=" + getId() +
            ", addressC='" + getAddressC() + "'" +
            ", dateC=" + getDateC() +
            ", client=" + getClient() +
            ", driver=" + getDriver() +
            "}";
    }
}
