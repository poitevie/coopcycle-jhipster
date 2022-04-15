package poitevie.coopcycle.service.mapper;

import org.mapstruct.*;
import poitevie.coopcycle.domain.Cooperative;
import poitevie.coopcycle.service.dto.CooperativeDTO;

/**
 * Mapper for the entity {@link Cooperative} and its DTO {@link CooperativeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CooperativeMapper extends EntityMapper<CooperativeDTO, Cooperative> {}
