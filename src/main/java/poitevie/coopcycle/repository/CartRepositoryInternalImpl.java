package poitevie.coopcycle.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import poitevie.coopcycle.domain.Cart;
import poitevie.coopcycle.repository.rowmapper.CartRowMapper;
import poitevie.coopcycle.repository.rowmapper.ClientRowMapper;
import poitevie.coopcycle.repository.rowmapper.CommandRowMapper;
import poitevie.coopcycle.repository.rowmapper.ShopRowMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Cart entity.
 */
@SuppressWarnings("unused")
class CartRepositoryInternalImpl extends SimpleR2dbcRepository<Cart, Long> implements CartRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CommandRowMapper commandMapper;
    private final ClientRowMapper clientMapper;
    private final ShopRowMapper shopMapper;
    private final CartRowMapper cartMapper;

    private static final Table entityTable = Table.aliased("cart", EntityManager.ENTITY_ALIAS);
    private static final Table commandTable = Table.aliased("command", "command");
    private static final Table clientTable = Table.aliased("client", "client");
    private static final Table shopTable = Table.aliased("shop", "shop");

    public CartRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CommandRowMapper commandMapper,
        ClientRowMapper clientMapper,
        ShopRowMapper shopMapper,
        CartRowMapper cartMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Cart.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.commandMapper = commandMapper;
        this.clientMapper = clientMapper;
        this.shopMapper = shopMapper;
        this.cartMapper = cartMapper;
    }

    @Override
    public Flux<Cart> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Cart> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CartSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CommandSqlHelper.getColumns(commandTable, "command"));
        columns.addAll(ClientSqlHelper.getColumns(clientTable, "client"));
        columns.addAll(ShopSqlHelper.getColumns(shopTable, "shop"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(commandTable)
            .on(Column.create("command_id", entityTable))
            .equals(Column.create("id", commandTable))
            .leftOuterJoin(clientTable)
            .on(Column.create("client_id", entityTable))
            .equals(Column.create("id", clientTable))
            .leftOuterJoin(shopTable)
            .on(Column.create("shop_id", entityTable))
            .equals(Column.create("id", shopTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Cart.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Cart> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Cart> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Cart process(Row row, RowMetadata metadata) {
        Cart entity = cartMapper.apply(row, "e");
        entity.setCommand(commandMapper.apply(row, "command"));
        entity.setClient(clientMapper.apply(row, "client"));
        entity.setShop(shopMapper.apply(row, "shop"));
        return entity;
    }

    @Override
    public <S extends Cart> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
