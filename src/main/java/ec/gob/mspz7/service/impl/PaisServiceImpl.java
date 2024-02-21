package ec.gob.mspz7.service.impl;

import ec.gob.mspz7.domain.Pais;
import ec.gob.mspz7.repository.PaisRepository;
import ec.gob.mspz7.repository.search.PaisSearchRepository;
import ec.gob.mspz7.service.PaisService;
import ec.gob.mspz7.service.dto.PaisDTO;
import ec.gob.mspz7.service.mapper.PaisMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ec.gob.mspz7.domain.Pais}.
 */
@Service
@Transactional
public class PaisServiceImpl implements PaisService {

    private final Logger log = LoggerFactory.getLogger(PaisServiceImpl.class);

    private final PaisRepository paisRepository;

    private final PaisMapper paisMapper;

    private final PaisSearchRepository paisSearchRepository;

    public PaisServiceImpl(PaisRepository paisRepository, PaisMapper paisMapper, PaisSearchRepository paisSearchRepository) {
        this.paisRepository = paisRepository;
        this.paisMapper = paisMapper;
        this.paisSearchRepository = paisSearchRepository;
    }

    @Override
    public PaisDTO save(PaisDTO paisDTO) {
        log.debug("Request to save Pais : {}", paisDTO);
        Pais pais = paisMapper.toEntity(paisDTO);
        pais = paisRepository.save(pais);
        PaisDTO result = paisMapper.toDto(pais);
        paisSearchRepository.index(pais);
        return result;
    }

    @Override
    public PaisDTO update(PaisDTO paisDTO) {
        log.debug("Request to update Pais : {}", paisDTO);
        Pais pais = paisMapper.toEntity(paisDTO);
        pais = paisRepository.save(pais);
        PaisDTO result = paisMapper.toDto(pais);
        paisSearchRepository.index(pais);
        return result;
    }

    @Override
    public Optional<PaisDTO> partialUpdate(PaisDTO paisDTO) {
        log.debug("Request to partially update Pais : {}", paisDTO);

        return paisRepository
            .findById(paisDTO.getId())
            .map(existingPais -> {
                paisMapper.partialUpdate(existingPais, paisDTO);

                return existingPais;
            })
            .map(paisRepository::save)
            .map(savedPais -> {
                paisSearchRepository.index(savedPais);
                return savedPais;
            })
            .map(paisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pais");
        return paisRepository.findAll(pageable).map(paisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaisDTO> findOne(Long id) {
        log.debug("Request to get Pais : {}", id);
        return paisRepository.findById(id).map(paisMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pais : {}", id);
        paisRepository.deleteById(id);
        paisSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaisDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Pais for query {}", query);
        return paisSearchRepository.search(query, pageable).map(paisMapper::toDto);
    }
}
