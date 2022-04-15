package poitevie.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Client.
 */
@Table("client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("id_c")
    private String idC;

    @NotNull(message = "must not be null")
    @Size(max = 20)
    @Pattern(regexp = "^[A-Z][a-z]+$")
    @Column("firstname_c")
    private String firstnameC;

    @NotNull(message = "must not be null")
    @Size(max = 20)
    @Pattern(regexp = "^[A-Z][a-z]+$")
    @Column("lastname_c")
    private String lastnameC;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\\\.]+)@([a-zA-Z0-9_\\-\\\\.]+)\\\\.([a-zA-Z]{2,5})$")
    @Column("email_c")
    private String emailC;

    @Column("phone_c")
    private String phoneC;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("address_c")
    private String addressC;

    @Transient
    @JsonIgnoreProperties(value = { "command", "client", "shop" }, allowSetters = true)
    private Set<Cart> carts = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "client", "cart", "driver" }, allowSetters = true)
    private Set<Command> commands = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdC() {
        return this.idC;
    }

    public Client idC(String idC) {
        this.setIdC(idC);
        return this;
    }

    public void setIdC(String idC) {
        this.idC = idC;
    }

    public String getFirstnameC() {
        return this.firstnameC;
    }

    public Client firstnameC(String firstnameC) {
        this.setFirstnameC(firstnameC);
        return this;
    }

    public void setFirstnameC(String firstnameC) {
        this.firstnameC = firstnameC;
    }

    public String getLastnameC() {
        return this.lastnameC;
    }

    public Client lastnameC(String lastnameC) {
        this.setLastnameC(lastnameC);
        return this;
    }

    public void setLastnameC(String lastnameC) {
        this.lastnameC = lastnameC;
    }

    public String getEmailC() {
        return this.emailC;
    }

    public Client emailC(String emailC) {
        this.setEmailC(emailC);
        return this;
    }

    public void setEmailC(String emailC) {
        this.emailC = emailC;
    }

    public String getPhoneC() {
        return this.phoneC;
    }

    public Client phoneC(String phoneC) {
        this.setPhoneC(phoneC);
        return this;
    }

    public void setPhoneC(String phoneC) {
        this.phoneC = phoneC;
    }

    public String getAddressC() {
        return this.addressC;
    }

    public Client addressC(String addressC) {
        this.setAddressC(addressC);
        return this;
    }

    public void setAddressC(String addressC) {
        this.addressC = addressC;
    }

    public Set<Cart> getCarts() {
        return this.carts;
    }

    public void setCarts(Set<Cart> carts) {
        if (this.carts != null) {
            this.carts.forEach(i -> i.setClient(null));
        }
        if (carts != null) {
            carts.forEach(i -> i.setClient(this));
        }
        this.carts = carts;
    }

    public Client carts(Set<Cart> carts) {
        this.setCarts(carts);
        return this;
    }

    public Client addCart(Cart cart) {
        this.carts.add(cart);
        cart.setClient(this);
        return this;
    }

    public Client removeCart(Cart cart) {
        this.carts.remove(cart);
        cart.setClient(null);
        return this;
    }

    public Set<Command> getCommands() {
        return this.commands;
    }

    public void setCommands(Set<Command> commands) {
        if (this.commands != null) {
            this.commands.forEach(i -> i.setClient(null));
        }
        if (commands != null) {
            commands.forEach(i -> i.setClient(this));
        }
        this.commands = commands;
    }

    public Client commands(Set<Command> commands) {
        this.setCommands(commands);
        return this;
    }

    public Client addCommand(Command command) {
        this.commands.add(command);
        command.setClient(this);
        return this;
    }

    public Client removeCommand(Command command) {
        this.commands.remove(command);
        command.setClient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
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
