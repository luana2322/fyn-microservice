package com.fyn.search.controller;

import com.fyn.common.dto.response.ApiResponse;
import com.fyn.search.document.SearchablePost;
import com.fyn.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<SearchablePost>>> searchPosts(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(searchService.searchPosts(q)));
    }
}
