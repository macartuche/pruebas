package ec.gob.mspz7.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import ec.gob.mspz7.domain.Catalogo;
import ec.gob.mspz7.repository.CatalogoRepository;
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
 * Spring Data Elasticsearch repository for the {@link Catalogo} entity.
 */
public interface CatalogoSearchRepository extends ElasticsearchRepository<Catalogo, Long>, CatalogoSearchRepositoryInternal {}

interface CatalogoSearchRepositoryInternal {
    Page<Catalogo> search(String query, Pageable pageable);

    Page<Catalogo> search(Query query);

    @Async
    void index(Catalogo entity);

    @Async
    void deleteFromIndexById(Long id);
}

class CatalogoSearchRepositoryInternalImpl implements CatalogoSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CatalogoRepository repository;

    CatalogoSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, CatalogoRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Catalogo> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Catalogo> search(Query query) {
        SearchHits<Catalogo> searchHits = elasticsearchTemplate.search(query, Catalogo.class);
        List<Catalogo> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Catalogo entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Catalogo.class);
    }
}
