package com.fyn.search.repository;

import com.fyn.search.document.SearchablePost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface PostSearchRepository extends ElasticsearchRepository<SearchablePost, String> {
    List<SearchablePost> findByContentContaining(String query);
}
