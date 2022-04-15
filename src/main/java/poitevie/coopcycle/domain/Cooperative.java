package poitevie.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Cooperative.
 */
@Table("cooperative")
public class Cooperative implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private String id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "commands", "cooperative" }, allowSetters = true)
    private Set<Driver> drivers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Cooperative id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cooperative name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Cooperative setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<Driver> getDrivers() {
        return this.drivers;
    }

    public void setDrivers(Set<Driver> drivers) {
        if (this.drivers != null) {
            this.drivers.forEach(i -> i.setCooperative(null));
        }
        if (drivers != null) {
            drivers.forEach(i -> i.setCooperative(this));
        }
        this.drivers = drivers;
    }

    public Cooperative drivers(Set<Driver> drivers) {
        this.setDrivers(drivers);
        return this;
    }

    public Cooperative addDriver(Driver driver) {
        this.drivers.add(driver);
        driver.setCooperative(this);
        return this;
    }

    public Cooperative removeDriver(Driver driver) {
        this.drivers.remove(driver);
        driver.setCooperative(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cooperative)) {
            return false;
        }
        return id != null && id.equals(((Cooperative) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cooperative{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
