package poitevie.coopcycle.service.mapper;

import org.mapstruct.*;
import poitevie.coopcycle.domain.Cooperative;
import poitevie.coopcycle.domain.Driver;
import poitevie.coopcycle.service.dto.CooperativeDTO;
import poitevie.coopcycle.service.dto.DriverDTO;

/**
 * Mapper for the entity {@link Driver} and its DTO {@link DriverDTO}.
 */
@Mapper(componentModel = "spring")
public interface DriverMapper extends EntityMapper<DriverDTO, Driver> {
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeId")
    DriverDTO toDto(Driver s);

    @Named("cooperativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CooperativeDTO toDtoCooperativeId(Cooperative cooperative);
}
