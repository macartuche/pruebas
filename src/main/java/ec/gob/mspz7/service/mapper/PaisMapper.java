package ec.gob.mspz7.service.mapper;

import ec.gob.mspz7.domain.Item;
import ec.gob.mspz7.domain.Pais;
import ec.gob.mspz7.service.dto.ItemDTO;
import ec.gob.mspz7.service.dto.PaisDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pais} and its DTO {@link PaisDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaisMapper extends EntityMapper<PaisDTO, Pais> {
    @Mapping(target = "region", source = "region", qualifiedByName = "itemId")
    PaisDTO toDto(Pais s);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemDTO toDtoItemId(Item item);
}
