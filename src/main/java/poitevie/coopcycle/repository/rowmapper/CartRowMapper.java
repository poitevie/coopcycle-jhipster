package poitevie.coopcycle.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import poitevie.coopcycle.domain.Cart;

/**
 * Converter between {@link Row} to {@link Cart}, with proper type conversions.
 */
@Service
public class CartRowMapper implements BiFunction<Row, String, Cart> {

    private final ColumnConverter converter;

    public CartRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Cart} stored in the database.
     */
    @Override
    public Cart apply(Row row, String prefix) {
        Cart entity = new Cart();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAmount(converter.fromRow(row, prefix + "_amount", Float.class));
        entity.setDeadline(converter.fromRow(row, prefix + "_deadline", Float.class));
        entity.setCommandId(converter.fromRow(row, prefix + "_command_id", Long.class));
        entity.setClientId(converter.fromRow(row, prefix + "_client_id", Long.class));
        entity.setShopId(converter.fromRow(row, prefix + "_shop_id", Long.class));
        return entity;
    }
}
