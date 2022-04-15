package poitevie.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Cart.
 */
@Table("cart")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    @Column("amount")
    private Float amount;

    @NotNull(message = "must not be null")
    @Column("deadline")
    private Float deadline;

    @Transient
    private Command command;

    @Transient
    @JsonIgnoreProperties(value = { "carts", "commands" }, allowSetters = true)
    private Client client;

    @Transient
    @JsonIgnoreProperties(value = { "carts" }, allowSetters = true)
    private Shop shop;

    @Column("command_id")
    private Long commandId;

    @Column("client_id")
    private Long clientId;

    @Column("shop_id")
    private Long shopId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cart id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAmount() {
        return this.amount;
    }

    public Cart amount(Float amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getDeadline() {
        return this.deadline;
    }

    public Cart deadline(Float deadline) {
        this.setDeadline(deadline);
        return this;
    }

    public void setDeadline(Float deadline) {
        this.deadline = deadline;
    }

    public Command getCommand() {
        return this.command;
    }

    public void setCommand(Command command) {
        this.command = command;
        this.commandId = command != null ? command.getId() : null;
    }

    public Cart command(Command command) {
        this.setCommand(command);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
        this.clientId = client != null ? client.getId() : null;
    }

    public Cart client(Client client) {
        this.setClient(client);
        return this;
    }

    public Shop getShop() {
        return this.shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
        this.shopId = shop != null ? shop.getId() : null;
    }

    public Cart shop(Shop shop) {
        this.setShop(shop);
        return this;
    }

    public Long getCommandId() {
        return this.commandId;
    }

    public void setCommandId(Long command) {
        this.commandId = command;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long client) {
        this.clientId = client;
    }

    public Long getShopId() {
        return this.shopId;
    }

    public void setShopId(Long shop) {
        this.shopId = shop;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cart)) {
            return false;
        }
        return id != null && id.equals(((Cart) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cart{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", deadline=" + getDeadline() +
            "}";
    }
}
