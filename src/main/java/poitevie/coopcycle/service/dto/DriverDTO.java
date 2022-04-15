package poitevie.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link poitevie.coopcycle.domain.Driver} entity.
 */
public class DriverDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Pattern(regexp = "^[A-Z][a-z]+$")
    private String firstnameD;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Pattern(regexp = "^[A-Z][a-z]+$")
    private String lastnameD;

    private String phoneD;

    private CooperativeDTO cooperative;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstnameD() {
        return firstnameD;
    }

    public void setFirstnameD(String firstnameD) {
        this.firstnameD = firstnameD;
    }

    public String getLastnameD() {
        return lastnameD;
    }

    public void setLastnameD(String lastnameD) {
        this.lastnameD = lastnameD;
    }

    public String getPhoneD() {
        return phoneD;
    }

    public void setPhoneD(String phoneD) {
        this.phoneD = phoneD;
    }

    public CooperativeDTO getCooperative() {
        return cooperative;
    }

    public void setCooperative(CooperativeDTO cooperative) {
        this.cooperative = cooperative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DriverDTO)) {
            return false;
        }

        DriverDTO driverDTO = (DriverDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, driverDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DriverDTO{" +
            "id=" + getId() +
            ", firstnameD='" + getFirstnameD() + "'" +
            ", lastnameD='" + getLastnameD() + "'" +
            ", phoneD='" + getPhoneD() + "'" +
            ", cooperative=" + getCooperative() +
            "}";
    }
}
