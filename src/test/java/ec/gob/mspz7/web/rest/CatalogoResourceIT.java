package ec.gob.mspz7.web.rest;

import static ec.gob.mspz7.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.gob.mspz7.IntegrationTest;
import ec.gob.mspz7.domain.Catalogo;
import ec.gob.mspz7.repository.CatalogoRepository;
import ec.gob.mspz7.repository.search.CatalogoSearchRepository;
import ec.gob.mspz7.service.dto.CatalogoDTO;
import ec.gob.mspz7.service.mapper.CatalogoMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CatalogoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_NUEVOCAMPO = "AAAAAAAAAA";
    private static final String UPDATED_NUEVOCAMPO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/catalogos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/catalogos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogoRepository catalogoRepository;

    @Autowired
    private CatalogoMapper catalogoMapper;

    @Autowired
    private CatalogoSearchRepository catalogoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogoMockMvc;

    private Catalogo catalogo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogo createEntity(EntityManager em) {
        Catalogo catalogo = new Catalogo()
            .codigo(DEFAULT_CODIGO)
            .descripcion(DEFAULT_DESCRIPCION)
            .nuevocampo(DEFAULT_NUEVOCAMPO)
            .valor(DEFAULT_VALOR)
            .activo(DEFAULT_ACTIVO);
        return catalogo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogo createUpdatedEntity(EntityManager em) {
        Catalogo catalogo = new Catalogo()
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .nuevocampo(UPDATED_NUEVOCAMPO)
            .valor(UPDATED_VALOR)
            .activo(UPDATED_ACTIVO);
        return catalogo;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        catalogoSearchRepository.deleteAll();
        assertThat(catalogoSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        catalogo = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalogo() throws Exception {
        int databaseSizeBeforeCreate = catalogoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);
        restCatalogoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogoDTO)))
            .andExpect(status().isCreated());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testCatalogo.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCatalogo.getNuevocampo()).isEqualTo(DEFAULT_NUEVOCAMPO);
        assertThat(testCatalogo.getValor()).isEqualByComparingTo(DEFAULT_VALOR);
        assertThat(testCatalogo.getActivo()).isEqualTo(DEFAULT_ACTIVO);
    }

    @Test
    @Transactional
    void createCatalogoWithExistingId() throws Exception {
        // Create the Catalogo with an existing ID
        catalogo.setId(1L);
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        int databaseSizeBeforeCreate = catalogoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(catalogoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        // set the field null
        catalogo.setCodigo(null);

        // Create the Catalogo, which fails.
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        restCatalogoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogoDTO)))
            .andExpect(status().isBadRequest());

        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCatalogos() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList
        restCatalogoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].nuevocampo").value(hasItem(DEFAULT_NUEVOCAMPO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }

    @Test
    @Transactional
    void getCatalogo() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        // Get the catalogo
        restCatalogoMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogo.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.nuevocampo").value(DEFAULT_NUEVOCAMPO))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCatalogo() throws Exception {
        // Get the catalogo
        restCatalogoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCatalogo() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        catalogoSearchRepository.save(catalogo);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(catalogoSearchRepository.findAll());

        // Update the catalogo
        Catalogo updatedCatalogo = catalogoRepository.findById(catalogo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCatalogo are not directly saved in db
        em.detach(updatedCatalogo);
        updatedCatalogo
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .nuevocampo(UPDATED_NUEVOCAMPO)
            .valor(UPDATED_VALOR)
            .activo(UPDATED_ACTIVO);
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(updatedCatalogo);

        restCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCatalogo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCatalogo.getNuevocampo()).isEqualTo(UPDATED_NUEVOCAMPO);
        assertThat(testCatalogo.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testCatalogo.getActivo()).isEqualTo(UPDATED_ACTIVO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Catalogo> catalogoSearchList = IterableUtils.toList(catalogoSearchRepository.findAll());
                Catalogo testCatalogoSearch = catalogoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testCatalogoSearch.getCodigo()).isEqualTo(UPDATED_CODIGO);
                assertThat(testCatalogoSearch.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
                assertThat(testCatalogoSearch.getNuevocampo()).isEqualTo(UPDATED_NUEVOCAMPO);
                assertThat(testCatalogoSearch.getValor()).isEqualByComparingTo(UPDATED_VALOR);
                assertThat(testCatalogoSearch.getActivo()).isEqualTo(UPDATED_ACTIVO);
            });
    }

    @Test
    @Transactional
    void putNonExistingCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        catalogo.setId(longCount.incrementAndGet());

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        catalogo.setId(longCount.incrementAndGet());

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        catalogo.setId(longCount.incrementAndGet());

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCatalogoWithPatch() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();

        // Update the catalogo using partial update
        Catalogo partialUpdatedCatalogo = new Catalogo();
        partialUpdatedCatalogo.setId(catalogo.getId());

        partialUpdatedCatalogo
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .nuevocampo(UPDATED_NUEVOCAMPO)
            .activo(UPDATED_ACTIVO);

        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogo))
            )
            .andExpect(status().isOk());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCatalogo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCatalogo.getNuevocampo()).isEqualTo(UPDATED_NUEVOCAMPO);
        assertThat(testCatalogo.getValor()).isEqualByComparingTo(DEFAULT_VALOR);
        assertThat(testCatalogo.getActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void fullUpdateCatalogoWithPatch() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();

        // Update the catalogo using partial update
        Catalogo partialUpdatedCatalogo = new Catalogo();
        partialUpdatedCatalogo.setId(catalogo.getId());

        partialUpdatedCatalogo
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .nuevocampo(UPDATED_NUEVOCAMPO)
            .valor(UPDATED_VALOR)
            .activo(UPDATED_ACTIVO);

        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogo))
            )
            .andExpect(status().isOk());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCatalogo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCatalogo.getNuevocampo()).isEqualTo(UPDATED_NUEVOCAMPO);
        assertThat(testCatalogo.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testCatalogo.getActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void patchNonExistingCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        catalogo.setId(longCount.incrementAndGet());

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        catalogo.setId(longCount.incrementAndGet());

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        catalogo.setId(longCount.incrementAndGet());

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(catalogoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCatalogo() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);
        catalogoRepository.save(catalogo);
        catalogoSearchRepository.save(catalogo);

        int databaseSizeBeforeDelete = catalogoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the catalogo
        restCatalogoMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(catalogoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCatalogo() throws Exception {
        // Initialize the database
        catalogo = catalogoRepository.saveAndFlush(catalogo);
        catalogoSearchRepository.save(catalogo);

        // Search the catalogo
        restCatalogoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + catalogo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].nuevocampo").value(hasItem(DEFAULT_NUEVOCAMPO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }
}
