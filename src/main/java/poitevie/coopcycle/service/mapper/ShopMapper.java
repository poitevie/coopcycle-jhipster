package poitevie.coopcycle.service.mapper;

import org.mapstruct.*;
import poitevie.coopcycle.domain.Shop;
import poitevie.coopcycle.service.dto.ShopDTO;

/**
 * Mapper for the entity {@link Shop} and its DTO {@link ShopDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShopMapper extends EntityMapper<ShopDTO, Shop> {}
