package poitevie.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link poitevie.coopcycle.domain.Shop} entity.
 */
public class ShopDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String addressS;

    private String menu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressS() {
        return addressS;
    }

    public void setAddressS(String addressS) {
        this.addressS = addressS;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopDTO)) {
            return false;
        }

        ShopDTO shopDTO = (ShopDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shopDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShopDTO{" +
            "id=" + getId() +
            ", addressS='" + getAddressS() + "'" +
            ", menu='" + getMenu() + "'" +
            "}";
    }
}
