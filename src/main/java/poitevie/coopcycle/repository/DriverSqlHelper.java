package poitevie.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DriverSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("firstname_d", table, columnPrefix + "_firstname_d"));
        columns.add(Column.aliased("lastname_d", table, columnPrefix + "_lastname_d"));
        columns.add(Column.aliased("phone_d", table, columnPrefix + "_phone_d"));

        columns.add(Column.aliased("cooperative_id", table, columnPrefix + "_cooperative_id"));
        return columns;
    }
}
