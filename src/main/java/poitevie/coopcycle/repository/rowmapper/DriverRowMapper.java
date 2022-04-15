package poitevie.coopcycle.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import poitevie.coopcycle.domain.Driver;

/**
 * Converter between {@link Row} to {@link Driver}, with proper type conversions.
 */
@Service
public class DriverRowMapper implements BiFunction<Row, String, Driver> {

    private final ColumnConverter converter;

    public DriverRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Driver} stored in the database.
     */
    @Override
    public Driver apply(Row row, String prefix) {
        Driver entity = new Driver();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFirstnameD(converter.fromRow(row, prefix + "_firstname_d", String.class));
        entity.setLastnameD(converter.fromRow(row, prefix + "_lastname_d", String.class));
        entity.setPhoneD(converter.fromRow(row, prefix + "_phone_d", String.class));
        entity.setCooperativeId(converter.fromRow(row, prefix + "_cooperative_id", String.class));
        return entity;
    }
}
