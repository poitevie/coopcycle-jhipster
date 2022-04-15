package poitevie.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CommandSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("address_c", table, columnPrefix + "_address_c"));
        columns.add(Column.aliased("date_c", table, columnPrefix + "_date_c"));

        columns.add(Column.aliased("client_id", table, columnPrefix + "_client_id"));
        columns.add(Column.aliased("driver_id", table, columnPrefix + "_driver_id"));
        return columns;
    }
}
