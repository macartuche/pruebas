package ec.gob.mspz7.service.impl;

import ec.gob.mspz7.domain.Catalogo;
import ec.gob.mspz7.repository.CatalogoRepository;
import ec.gob.mspz7.repository.search.CatalogoSearchRepository;
import ec.gob.mspz7.service.CatalogoService;
import ec.gob.mspz7.service.dto.CatalogoDTO;
import ec.gob.mspz7.service.mapper.CatalogoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ec.gob.mspz7.domain.Catalogo}.
 */
@Service
@Transactional
public class CatalogoServiceImpl implements CatalogoService {

    private final Logger log = LoggerFactory.getLogger(CatalogoServiceImpl.class);

    private final CatalogoRepository catalogoRepository;

    private final CatalogoMapper catalogoMapper;

    private final CatalogoSearchRepository catalogoSearchRepository;

    public CatalogoServiceImpl(
        CatalogoRepository catalogoRepository,
        CatalogoMapper catalogoMapper,
        CatalogoSearchRepository catalogoSearchRepository
    ) {
        this.catalogoRepository = catalogoRepository;
        this.catalogoMapper = catalogoMapper;
        this.catalogoSearchRepository = catalogoSearchRepository;
    }

    @Override
    public CatalogoDTO save(CatalogoDTO catalogoDTO) {
        log.debug("Request to save Catalogo : {}", catalogoDTO);
        Catalogo catalogo = catalogoMapper.toEntity(catalogoDTO);
        catalogo = catalogoRepository.save(catalogo);
        CatalogoDTO result = catalogoMapper.toDto(catalogo);
        catalogoSearchRepository.index(catalogo);
        return result;
    }

    @Override
    public CatalogoDTO update(CatalogoDTO catalogoDTO) {
        log.debug("Request to update Catalogo : {}", catalogoDTO);
        Catalogo catalogo = catalogoMapper.toEntity(catalogoDTO);
        catalogo = catalogoRepository.save(catalogo);
        CatalogoDTO result = catalogoMapper.toDto(catalogo);
        catalogoSearchRepository.index(catalogo);
        return result;
    }

    @Override
    public Optional<CatalogoDTO> partialUpdate(CatalogoDTO catalogoDTO) {
        log.debug("Request to partially update Catalogo : {}", catalogoDTO);

        return catalogoRepository
            .findById(catalogoDTO.getId())
            .map(existingCatalogo -> {
                catalogoMapper.partialUpdate(existingCatalogo, catalogoDTO);

                return existingCatalogo;
            })
            .map(catalogoRepository::save)
            .map(savedCatalogo -> {
                catalogoSearchRepository.index(savedCatalogo);
                return savedCatalogo;
            })
            .map(catalogoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CatalogoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Catalogos");
        return catalogoRepository.findAll(pageable).map(catalogoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CatalogoDTO> findOne(Long id) {
        log.debug("Request to get Catalogo : {}", id);
        return catalogoRepository.findById(id).map(catalogoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Catalogo : {}", id);
        catalogoRepository.deleteById(id);
        catalogoSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CatalogoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Catalogos for query {}", query);
        return catalogoSearchRepository.search(query, pageable).map(catalogoMapper::toDto);
    }
}
