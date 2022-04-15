package poitevie.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link poitevie.coopcycle.domain.Client} entity.
 */
public class ClientDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String idC;

    @NotNull(message = "must not be null")
    @Size(max = 20)
    @Pattern(regexp = "^[A-Z][a-z]+$")
    private String firstnameC;

    @NotNull(message = "must not be null")
    @Size(max = 20)
    @Pattern(regexp = "^[A-Z][a-z]+$")
    private String lastnameC;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\\\.]+)@([a-zA-Z0-9_\\-\\\\.]+)\\\\.([a-zA-Z]{2,5})$")
    private String emailC;

    private String phoneC;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    private String addressC;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdC() {
        return idC;
    }

    public void setIdC(String idC) {
        this.idC = idC;
    }

    public String getFirstnameC() {
        return firstnameC;
    }

    public void setFirstnameC(String firstnameC) {
        this.firstnameC = firstnameC;
    }

    public String getLastnameC() {
        return lastnameC;
    }

    public void setLastnameC(String lastnameC) {
        this.lastnameC = lastnameC;
    }

    public String getEmailC() {
        return emailC;
    }

    public void setEmailC(String emailC) {
        this.emailC = emailC;
    }

    public String getPhoneC() {
        return phoneC;
    }

    public void setPhoneC(String phoneC) {
        this.phoneC = phoneC;
    }

    public String getAddressC() {
        return addressC;
    }

    public void setAddressC(String addressC) {
        this.addressC = addressC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientDTO)) {
            return false;
        }

        ClientDTO clientDTO = (ClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + getId() +
            ", idC='" + getIdC() + "'" +
            ", firstnameC='" + getFirstnameC() + "'" +
            ", lastnameC='" + getLastnameC() + "'" +
            ", emailC='" + getEmailC() + "'" +
            ", phoneC='" + getPhoneC() + "'" +
            ", addressC='" + getAddressC() + "'" +
            "}";
    }
}
