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
 * A Shop.
 */
@Table("shop")
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("address_s")
    private String addressS;

    @Column("menu")
    private String menu;

    @Transient
    @JsonIgnoreProperties(value = { "command", "client", "shop" }, allowSetters = true)
    private Set<Cart> carts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Shop id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressS() {
        return this.addressS;
    }

    public Shop addressS(String addressS) {
        this.setAddressS(addressS);
        return this;
    }

    public void setAddressS(String addressS) {
        this.addressS = addressS;
    }

    public String getMenu() {
        return this.menu;
    }

    public Shop menu(String menu) {
        this.setMenu(menu);
        return this;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Set<Cart> getCarts() {
        return this.carts;
    }

    public void setCarts(Set<Cart> carts) {
        if (this.carts != null) {
            this.carts.forEach(i -> i.setShop(null));
        }
        if (carts != null) {
            carts.forEach(i -> i.setShop(this));
        }
        this.carts = carts;
    }

    public Shop carts(Set<Cart> carts) {
        this.setCarts(carts);
        return this;
    }

    public Shop addCart(Cart cart) {
        this.carts.add(cart);
        cart.setShop(this);
        return this;
    }

    public Shop removeCart(Cart cart) {
        this.carts.remove(cart);
        cart.setShop(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shop)) {
            return false;
        }
        return id != null && id.equals(((Shop) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shop{" +
            "id=" + getId() +
            ", addressS='" + getAddressS() + "'" +
            ", menu='" + getMenu() + "'" +
            "}";
    }
}
