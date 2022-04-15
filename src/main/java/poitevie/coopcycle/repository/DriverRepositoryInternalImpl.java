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
import poitevie.coopcycle.domain.Driver;
import poitevie.coopcycle.repository.rowmapper.CooperativeRowMapper;
import poitevie.coopcycle.repository.rowmapper.DriverRowMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Driver entity.
 */
@SuppressWarnings("unused")
class DriverRepositoryInternalImpl extends SimpleR2dbcRepository<Driver, Long> implements DriverRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CooperativeRowMapper cooperativeMapper;
    private final DriverRowMapper driverMapper;

    private static final Table entityTable = Table.aliased("driver", EntityManager.ENTITY_ALIAS);
    private static final Table cooperativeTable = Table.aliased("cooperative", "cooperative");

    public DriverRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CooperativeRowMapper cooperativeMapper,
        DriverRowMapper driverMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Driver.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.cooperativeMapper = cooperativeMapper;
        this.driverMapper = driverMapper;
    }

    @Override
    public Flux<Driver> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Driver> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = DriverSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CooperativeSqlHelper.getColumns(cooperativeTable, "cooperative"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(cooperativeTable)
            .on(Column.create("cooperative_id", entityTable))
            .equals(Column.create("id", cooperativeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Driver.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Driver> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Driver> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Driver process(Row row, RowMetadata metadata) {
        Driver entity = driverMapper.apply(row, "e");
        entity.setCooperative(cooperativeMapper.apply(row, "cooperative"));
        return entity;
    }

    @Override
    public <S extends Driver> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
