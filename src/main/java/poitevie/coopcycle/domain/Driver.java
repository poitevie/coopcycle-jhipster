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
 * A Driver.
 */
@Table("driver")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Pattern(regexp = "^[A-Z][a-z]+$")
    @Column("firstname_d")
    private String firstnameD;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Pattern(regexp = "^[A-Z][a-z]+$")
    @Column("lastname_d")
    private String lastnameD;

    @Column("phone_d")
    private String phoneD;

    @Transient
    @JsonIgnoreProperties(value = { "client", "cart", "driver" }, allowSetters = true)
    private Set<Command> commands = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "drivers" }, allowSetters = true)
    private Cooperative cooperative;

    @Column("cooperative_id")
    private String cooperativeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Driver id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstnameD() {
        return this.firstnameD;
    }

    public Driver firstnameD(String firstnameD) {
        this.setFirstnameD(firstnameD);
        return this;
    }

    public void setFirstnameD(String firstnameD) {
        this.firstnameD = firstnameD;
    }

    public String getLastnameD() {
        return this.lastnameD;
    }

    public Driver lastnameD(String lastnameD) {
        this.setLastnameD(lastnameD);
        return this;
    }

    public void setLastnameD(String lastnameD) {
        this.lastnameD = lastnameD;
    }

    public String getPhoneD() {
        return this.phoneD;
    }

    public Driver phoneD(String phoneD) {
        this.setPhoneD(phoneD);
        return this;
    }

    public void setPhoneD(String phoneD) {
        this.phoneD = phoneD;
    }

    public Set<Command> getCommands() {
        return this.commands;
    }

    public void setCommands(Set<Command> commands) {
        if (this.commands != null) {
            this.commands.forEach(i -> i.setDriver(null));
        }
        if (commands != null) {
            commands.forEach(i -> i.setDriver(this));
        }
        this.commands = commands;
    }

    public Driver commands(Set<Command> commands) {
        this.setCommands(commands);
        return this;
    }

    public Driver addCommand(Command command) {
        this.commands.add(command);
        command.setDriver(this);
        return this;
    }

    public Driver removeCommand(Command command) {
        this.commands.remove(command);
        command.setDriver(null);
        return this;
    }

    public Cooperative getCooperative() {
        return this.cooperative;
    }

    public void setCooperative(Cooperative cooperative) {
        this.cooperative = cooperative;
        this.cooperativeId = cooperative != null ? cooperative.getId() : null;
    }

    public Driver cooperative(Cooperative cooperative) {
        this.setCooperative(cooperative);
        return this;
    }

    public String getCooperativeId() {
        return this.cooperativeId;
    }

    public void setCooperativeId(String cooperative) {
        this.cooperativeId = cooperative;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Driver)) {
            return false;
        }
        return id != null && id.equals(((Driver) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Driver{" +
            "id=" + getId() +
            ", firstnameD='" + getFirstnameD() + "'" +
            ", lastnameD='" + getLastnameD() + "'" +
            ", phoneD='" + getPhoneD() + "'" +
            "}";
    }
}
