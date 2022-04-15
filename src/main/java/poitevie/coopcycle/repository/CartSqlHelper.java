package poitevie.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CartSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("amount", table, columnPrefix + "_amount"));
        columns.add(Column.aliased("deadline", table, columnPrefix + "_deadline"));

        columns.add(Column.aliased("command_id", table, columnPrefix + "_command_id"));
        columns.add(Column.aliased("client_id", table, columnPrefix + "_client_id"));
        columns.add(Column.aliased("shop_id", table, columnPrefix + "_shop_id"));
        return columns;
    }
}
