package poitevie.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ClientSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_c", table, columnPrefix + "_id_c"));
        columns.add(Column.aliased("firstname_c", table, columnPrefix + "_firstname_c"));
        columns.add(Column.aliased("lastname_c", table, columnPrefix + "_lastname_c"));
        columns.add(Column.aliased("email_c", table, columnPrefix + "_email_c"));
        columns.add(Column.aliased("phone_c", table, columnPrefix + "_phone_c"));
        columns.add(Column.aliased("address_c", table, columnPrefix + "_address_c"));

        return columns;
    }
}
