package poitevie.coopcycle.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import poitevie.coopcycle.domain.Command;

/**
 * Converter between {@link Row} to {@link Command}, with proper type conversions.
 */
@Service
public class CommandRowMapper implements BiFunction<Row, String, Command> {

    private final ColumnConverter converter;

    public CommandRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Command} stored in the database.
     */
    @Override
    public Command apply(Row row, String prefix) {
        Command entity = new Command();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAddressC(converter.fromRow(row, prefix + "_address_c", String.class));
        entity.setDateC(converter.fromRow(row, prefix + "_date_c", Float.class));
        entity.setClientId(converter.fromRow(row, prefix + "_client_id", Long.class));
        entity.setDriverId(converter.fromRow(row, prefix + "_driver_id", Long.class));
        return entity;
    }
}
