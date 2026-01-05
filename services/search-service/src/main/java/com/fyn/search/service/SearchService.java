package com.fyn.search.service;

import com.fyn.search.document.SearchablePost;
import com.fyn.search.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final PostSearchRepository postSearchRepository;

    public void indexPost(SearchablePost post) {
        postSearchRepository.save(post);
    }

    public List<SearchablePost> searchPosts(String query) {
        return postSearchRepository.findByContentContaining(query);
    }
}
