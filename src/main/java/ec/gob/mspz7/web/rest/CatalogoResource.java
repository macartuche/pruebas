package ec.gob.mspz7.web.rest;

import ec.gob.mspz7.repository.CatalogoRepository;
import ec.gob.mspz7.service.CatalogoService;
import ec.gob.mspz7.service.dto.CatalogoDTO;
import ec.gob.mspz7.web.rest.errors.BadRequestAlertException;
import ec.gob.mspz7.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ec.gob.mspz7.domain.Catalogo}.
 */
@RestController
@RequestMapping("/api/catalogos")
public class CatalogoResource {

    private final Logger log = LoggerFactory.getLogger(CatalogoResource.class);

    private static final String ENTITY_NAME = "catalogo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogoService catalogoService;

    private final CatalogoRepository catalogoRepository;

    public CatalogoResource(CatalogoService catalogoService, CatalogoRepository catalogoRepository) {
        this.catalogoService = catalogoService;
        this.catalogoRepository = catalogoRepository;
    }

    /**
     * {@code POST  /catalogos} : Create a new catalogo.
     *
     * @param catalogoDTO the catalogoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogoDTO, or with status {@code 400 (Bad Request)} if the catalogo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CatalogoDTO> createCatalogo(@Valid @RequestBody CatalogoDTO catalogoDTO) throws URISyntaxException {
        log.debug("REST request to save Catalogo : {}", catalogoDTO);
        if (catalogoDTO.getId() != null) {
            throw new BadRequestAlertException("A new catalogo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogoDTO result = catalogoService.save(catalogoDTO);
        return ResponseEntity
            .created(new URI("/api/catalogos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalogos/:id} : Updates an existing catalogo.
     *
     * @param id the id of the catalogoDTO to save.
     * @param catalogoDTO the catalogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogoDTO,
     * or with status {@code 400 (Bad Request)} if the catalogoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CatalogoDTO> updateCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogoDTO catalogoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Catalogo : {}, {}", id, catalogoDTO);
        if (catalogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogoDTO result = catalogoService.update(catalogoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalogos/:id} : Partial updates given fields of an existing catalogo, field will ignore if it is null
     *
     * @param id the id of the catalogoDTO to save.
     * @param catalogoDTO the catalogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogoDTO,
     * or with status {@code 400 (Bad Request)} if the catalogoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the catalogoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogoDTO> partialUpdateCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogoDTO catalogoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Catalogo partially : {}, {}", id, catalogoDTO);
        if (catalogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogoDTO> result = catalogoService.partialUpdate(catalogoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /catalogos} : get all the catalogos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CatalogoDTO>> getAllCatalogos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Catalogos");
        Page<CatalogoDTO> page = catalogoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalogos/:id} : get the "id" catalogo.
     *
     * @param id the id of the catalogoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CatalogoDTO> getCatalogo(@PathVariable("id") Long id) {
        log.debug("REST request to get Catalogo : {}", id);
        Optional<CatalogoDTO> catalogoDTO = catalogoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogoDTO);
    }

    /**
     * {@code DELETE  /catalogos/:id} : delete the "id" catalogo.
     *
     * @param id the id of the catalogoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalogo(@PathVariable("id") Long id) {
        log.debug("REST request to delete Catalogo : {}", id);
        catalogoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /catalogos/_search?query=:query} : search for the catalogo corresponding
     * to the query.
     *
     * @param query the query of the catalogo search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<CatalogoDTO>> searchCatalogos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Catalogos for query {}", query);
        try {
            Page<CatalogoDTO> page = catalogoService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
