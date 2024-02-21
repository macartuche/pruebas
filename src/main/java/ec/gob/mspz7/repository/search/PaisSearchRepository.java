package ec.gob.mspz7.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import ec.gob.mspz7.domain.Pais;
import ec.gob.mspz7.repository.PaisRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Pais} entity.
 */
public interface PaisSearchRepository extends ElasticsearchRepository<Pais, Long>, PaisSearchRepositoryInternal {}

interface PaisSearchRepositoryInternal {
    Page<Pais> search(String query, Pageable pageable);

    Page<Pais> search(Query query);

    @Async
    void index(Pais entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PaisSearchRepositoryInternalImpl implements PaisSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PaisRepository repository;

    PaisSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PaisRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Pais> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Pais> search(Query query) {
        SearchHits<Pais> searchHits = elasticsearchTemplate.search(query, Pais.class);
        List<Pais> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Pais entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Pais.class);
    }
}
