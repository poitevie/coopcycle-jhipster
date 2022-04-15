package poitevie.coopcycle.service.mapper;

import org.mapstruct.*;
import poitevie.coopcycle.domain.Client;
import poitevie.coopcycle.domain.Command;
import poitevie.coopcycle.domain.Driver;
import poitevie.coopcycle.service.dto.ClientDTO;
import poitevie.coopcycle.service.dto.CommandDTO;
import poitevie.coopcycle.service.dto.DriverDTO;

/**
 * Mapper for the entity {@link Command} and its DTO {@link CommandDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommandMapper extends EntityMapper<CommandDTO, Command> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "driver", source = "driver", qualifiedByName = "driverId")
    CommandDTO toDto(Command s);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("driverId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DriverDTO toDtoDriverId(Driver driver);
}
