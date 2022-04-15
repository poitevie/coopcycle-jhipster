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
import poitevie.coopcycle.domain.Command;
import poitevie.coopcycle.repository.rowmapper.ClientRowMapper;
import poitevie.coopcycle.repository.rowmapper.CommandRowMapper;
import poitevie.coopcycle.repository.rowmapper.DriverRowMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Command entity.
 */
@SuppressWarnings("unused")
class CommandRepositoryInternalImpl extends SimpleR2dbcRepository<Command, Long> implements CommandRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ClientRowMapper clientMapper;
    private final DriverRowMapper driverMapper;
    private final CommandRowMapper commandMapper;

    private static final Table entityTable = Table.aliased("command", EntityManager.ENTITY_ALIAS);
    private static final Table clientTable = Table.aliased("client", "client");
    private static final Table driverTable = Table.aliased("driver", "driver");

    public CommandRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ClientRowMapper clientMapper,
        DriverRowMapper driverMapper,
        CommandRowMapper commandMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Command.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.clientMapper = clientMapper;
        this.driverMapper = driverMapper;
        this.commandMapper = commandMapper;
    }

    @Override
    public Flux<Command> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Command> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CommandSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ClientSqlHelper.getColumns(clientTable, "client"));
        columns.addAll(DriverSqlHelper.getColumns(driverTable, "driver"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(clientTable)
            .on(Column.create("client_id", entityTable))
            .equals(Column.create("id", clientTable))
            .leftOuterJoin(driverTable)
            .on(Column.create("driver_id", entityTable))
            .equals(Column.create("id", driverTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Command.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Command> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Command> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Command process(Row row, RowMetadata metadata) {
        Command entity = commandMapper.apply(row, "e");
        entity.setClient(clientMapper.apply(row, "client"));
        entity.setDriver(driverMapper.apply(row, "driver"));
        return entity;
    }

    @Override
    public <S extends Command> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
