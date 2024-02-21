package ec.gob.mspz7.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import ec.gob.mspz7.domain.Item;
import ec.gob.mspz7.repository.ItemRepository;
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
 * Spring Data Elasticsearch repository for the {@link Item} entity.
 */
public interface ItemSearchRepository extends ElasticsearchRepository<Item, Long>, ItemSearchRepositoryInternal {}

interface ItemSearchRepositoryInternal {
    Page<Item> search(String query, Pageable pageable);

    Page<Item> search(Query query);

    @Async
    void index(Item entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ItemSearchRepositoryInternalImpl implements ItemSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ItemRepository repository;

    ItemSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ItemRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Item> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Item> search(Query query) {
        SearchHits<Item> searchHits = elasticsearchTemplate.search(query, Item.class);
        List<Item> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Item entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Item.class);
    }
}
