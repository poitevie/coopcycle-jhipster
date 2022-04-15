package poitevie.coopcycle.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import poitevie.coopcycle.domain.Client;

/**
 * Converter between {@link Row} to {@link Client}, with proper type conversions.
 */
@Service
public class ClientRowMapper implements BiFunction<Row, String, Client> {

    private final ColumnConverter converter;

    public ClientRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Client} stored in the database.
     */
    @Override
    public Client apply(Row row, String prefix) {
        Client entity = new Client();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdC(converter.fromRow(row, prefix + "_id_c", String.class));
        entity.setFirstnameC(converter.fromRow(row, prefix + "_firstname_c", String.class));
        entity.setLastnameC(converter.fromRow(row, prefix + "_lastname_c", String.class));
        entity.setEmailC(converter.fromRow(row, prefix + "_email_c", String.class));
        entity.setPhoneC(converter.fromRow(row, prefix + "_phone_c", String.class));
        entity.setAddressC(converter.fromRow(row, prefix + "_address_c", String.class));
        return entity;
    }
}
