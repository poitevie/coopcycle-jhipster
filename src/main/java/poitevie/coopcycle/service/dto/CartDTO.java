package poitevie.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link poitevie.coopcycle.domain.Cart} entity.
 */
public class CartDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    private Float amount;

    @NotNull(message = "must not be null")
    private Float deadline;

    private CommandDTO command;

    private ClientDTO client;

    private ShopDTO shop;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getDeadline() {
        return deadline;
    }

    public void setDeadline(Float deadline) {
        this.deadline = deadline;
    }

    public CommandDTO getCommand() {
        return command;
    }

    public void setCommand(CommandDTO command) {
        this.command = command;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public ShopDTO getShop() {
        return shop;
    }

    public void setShop(ShopDTO shop) {
        this.shop = shop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartDTO)) {
            return false;
        }

        CartDTO cartDTO = (CartDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cartDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", deadline=" + getDeadline() +
            ", command=" + getCommand() +
            ", client=" + getClient() +
            ", shop=" + getShop() +
            "}";
    }
}
