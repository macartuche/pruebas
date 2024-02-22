package ec.gob.mspz7.service.impl;

import ec.gob.mspz7.domain.Item;
import ec.gob.mspz7.repository.ItemRepository;
import ec.gob.mspz7.repository.search.ItemSearchRepository;
import ec.gob.mspz7.service.ItemService;
import ec.gob.mspz7.service.dto.ItemDTO;
import ec.gob.mspz7.service.mapper.ItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ec.gob.mspz7.domain.Item}.
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final ItemSearchRepository itemSearchRepository;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper, ItemSearchRepository itemSearchRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.itemSearchRepository = itemSearchRepository;
    }

    @Override
    public ItemDTO save(ItemDTO itemDTO) {
        log.debug("Request to save Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        ItemDTO result = itemMapper.toDto(item);
        itemSearchRepository.index(item);
        return result;
    }

    @Override
    public ItemDTO update(ItemDTO itemDTO) {
        log.debug("Request to update Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        ItemDTO result = itemMapper.toDto(item);
        itemSearchRepository.index(item);
        return result;
    }

    @Override
    public Optional<ItemDTO> partialUpdate(ItemDTO itemDTO) {
        log.debug("Request to partially update Item : {}", itemDTO);

        return itemRepository
            .findById(itemDTO.getId())
            .map(existingItem -> {
                itemMapper.partialUpdate(existingItem, itemDTO);

                return existingItem;
            })
            .map(itemRepository::save)
            .map(savedItem -> {
                itemSearchRepository.index(savedItem);
                return savedItem;
            })
            .map(itemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAll(pageable).map(itemMapper::toDto);
    }

    /**
     *  Get all the items where Pais is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ItemDTO> findAllWherePaisIsNull() {
        log.debug("Request to get all items where Pais is null");
        return StreamSupport
            .stream(itemRepository.findAll().spliterator(), false)
            .filter(item -> item.getPais() == null)
            .map(itemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemDTO> findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findById(id).map(itemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);

        itemRepository.deleteById(id);
        itemSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Items for query {}", query);
        return itemSearchRepository.search(query, pageable).map(itemMapper::toDto);
    }
}
