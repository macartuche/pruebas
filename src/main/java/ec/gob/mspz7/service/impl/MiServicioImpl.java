package ec.gob.mspz7.service.impl;

import ec.gob.mspz7.domain.Item;
import ec.gob.mspz7.repository.ItemRepository;
import ec.gob.mspz7.service.MiServicio;
import ec.gob.mspz7.service.dto.ItemDTO;
import ec.gob.mspz7.service.mapper.ItemMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MiServicioImpl implements MiServicio {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public MiServicioImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public List<Item> obtenerPorCodigoCatalogo(String codigoCatalogo) {
        return itemRepository.obtenerItemsPorCodigoCatalogo(codigoCatalogo);
    }

    @Override
    public ItemDTO guardar(ItemDTO dto) {
        Item item = itemRepository.save(itemMapper.toEntity(dto));
        return itemMapper.toDto(item);
    }

    @Override
    public List<ItemDTO> obtenerItemsDTOPorCodigoCatalogo(String codigoCatalogo) {
        List<Item> items = itemRepository.obtenerItemsPorCodigoCatalogo(codigoCatalogo);
        return itemMapper.toDto(items);
    }
}
