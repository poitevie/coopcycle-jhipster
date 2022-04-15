package poitevie.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Command.
 */
@Table("command")
public class Command implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("address_c")
    private String addressC;

    @NotNull(message = "must not be null")
    @Column("date_c")
    private Float dateC;

    @Transient
    @JsonIgnoreProperties(value = { "carts", "commands" }, allowSetters = true)
    private Client client;

    @Transient
    private Cart cart;

    @Transient
    @JsonIgnoreProperties(value = { "commands", "cooperative" }, allowSetters = true)
    private Driver driver;

    @Column("client_id")
    private Long clientId;

    @Column("driver_id")
    private Long driverId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Command id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressC() {
        return this.addressC;
    }

    public Command addressC(String addressC) {
        this.setAddressC(addressC);
        return this;
    }

    public void setAddressC(String addressC) {
        this.addressC = addressC;
    }

    public Float getDateC() {
        return this.dateC;
    }

    public Command dateC(Float dateC) {
        this.setDateC(dateC);
        return this;
    }

    public void setDateC(Float dateC) {
        this.dateC = dateC;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
        this.clientId = client != null ? client.getId() : null;
    }

    public Command client(Client client) {
        this.setClient(client);
        return this;
    }

    public Cart getCart() {
        return this.cart;
    }

    public void setCart(Cart cart) {
        if (this.cart != null) {
            this.cart.setCommand(null);
        }
        if (cart != null) {
            cart.setCommand(this);
        }
        this.cart = cart;
    }

    public Command cart(Cart cart) {
        this.setCart(cart);
        return this;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        this.driverId = driver != null ? driver.getId() : null;
    }

    public Command driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long client) {
        this.clientId = client;
    }

    public Long getDriverId() {
        return this.driverId;
    }

    public void setDriverId(Long driver) {
        this.driverId = driver;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Command)) {
            return false;
        }
        return id != null && id.equals(((Command) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Command{" +
            "id=" + getId() +
            ", addressC='" + getAddressC() + "'" +
            ", dateC=" + getDateC() +
            "}";
    }
}
