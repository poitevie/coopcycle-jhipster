package poitevie.coopcycle.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import poitevie.coopcycle.domain.Shop;

/**
 * Converter between {@link Row} to {@link Shop}, with proper type conversions.
 */
@Service
public class ShopRowMapper implements BiFunction<Row, String, Shop> {

    private final ColumnConverter converter;

    public ShopRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Shop} stored in the database.
     */
    @Override
    public Shop apply(Row row, String prefix) {
        Shop entity = new Shop();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAddressS(converter.fromRow(row, prefix + "_address_s", String.class));
        entity.setMenu(converter.fromRow(row, prefix + "_menu", String.class));
        return entity;
    }
}
