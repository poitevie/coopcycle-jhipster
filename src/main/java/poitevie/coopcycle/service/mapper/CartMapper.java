package poitevie.coopcycle.service.mapper;

import org.mapstruct.*;
import poitevie.coopcycle.domain.Cart;
import poitevie.coopcycle.domain.Client;
import poitevie.coopcycle.domain.Command;
import poitevie.coopcycle.domain.Shop;
import poitevie.coopcycle.service.dto.CartDTO;
import poitevie.coopcycle.service.dto.ClientDTO;
import poitevie.coopcycle.service.dto.CommandDTO;
import poitevie.coopcycle.service.dto.ShopDTO;

/**
 * Mapper for the entity {@link Cart} and its DTO {@link CartDTO}.
 */
@Mapper(componentModel = "spring")
public interface CartMapper extends EntityMapper<CartDTO, Cart> {
    @Mapping(target = "command", source = "command", qualifiedByName = "commandId")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "shop", source = "shop", qualifiedByName = "shopId")
    CartDTO toDto(Cart s);

    @Named("commandId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommandDTO toDtoCommandId(Command command);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("shopId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShopDTO toDtoShopId(Shop shop);
}
