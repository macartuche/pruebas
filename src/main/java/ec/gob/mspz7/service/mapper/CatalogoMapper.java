package ec.gob.mspz7.service.mapper;

import ec.gob.mspz7.domain.Catalogo;
import ec.gob.mspz7.service.dto.CatalogoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Catalogo} and its DTO {@link CatalogoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CatalogoMapper extends EntityMapper<CatalogoDTO, Catalogo> {}
