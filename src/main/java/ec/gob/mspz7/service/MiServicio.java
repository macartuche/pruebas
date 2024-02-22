package ec.gob.mspz7.service;

import ec.gob.mspz7.domain.Item;
import ec.gob.mspz7.service.dto.ItemDTO;
import java.util.List;

public interface MiServicio {
    /**
     * Listado de items por codigo Catalogo
     * @param codigoCatalogo String
     * @return List<Item>
     */
    List<Item> obtenerPorCodigoCatalogo(String codigoCatalogo);

    ItemDTO guardar(ItemDTO dto);

    List<ItemDTO> obtenerItemsDTOPorCodigoCatalogo(String codigoCatalogo);
}
