package ec.gob.mspz7.service.mapper;

import ec.gob.mspz7.domain.Catalogo;
import ec.gob.mspz7.domain.Item;
import ec.gob.mspz7.service.dto.CatalogoDTO;
import ec.gob.mspz7.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(target = "catalogo", source = "catalogo", qualifiedByName = "catalogoId")
    ItemDTO toDto(Item s);

    @Named("catalogoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CatalogoDTO toDtoCatalogoId(Catalogo catalogo);
}
