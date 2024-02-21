package ec.gob.mspz7.service;

import ec.gob.mspz7.service.dto.CatalogoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.gob.mspz7.domain.Catalogo}.
 */
public interface CatalogoService {
    /**
     * Save a catalogo.
     *
     * @param catalogoDTO the entity to save.
     * @return the persisted entity.
     */
    CatalogoDTO save(CatalogoDTO catalogoDTO);

    /**
     * Updates a catalogo.
     *
     * @param catalogoDTO the entity to update.
     * @return the persisted entity.
     */
    CatalogoDTO update(CatalogoDTO catalogoDTO);

    /**
     * Partially updates a catalogo.
     *
     * @param catalogoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CatalogoDTO> partialUpdate(CatalogoDTO catalogoDTO);

    /**
     * Get all the catalogos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CatalogoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" catalogo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CatalogoDTO> findOne(Long id);

    /**
     * Delete the "id" catalogo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the catalogo corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CatalogoDTO> search(String query, Pageable pageable);
}
