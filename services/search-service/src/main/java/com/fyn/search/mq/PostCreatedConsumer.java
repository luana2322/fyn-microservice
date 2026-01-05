package com.fyn.search.mq;

import com.fyn.search.document.SearchablePost;
import com.fyn.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostCreatedConsumer {
    private final SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "search-service.post.queue", durable = "true"), exchange = @Exchange(name = "post-exchange", type = "topic"), key = "post.created"))
    public void handlePostCreated(Map<String, Object> event) {
        log.info("Received post created event for indexing: {}", event.get("postId"));

        SearchablePost post = SearchablePost.builder()
                .id(event.get("postId").toString())
                .content(event.get("content").toString())
                .authorId(event.get("authorId").toString())
                .build();

        searchService.indexPost(post);
    }
}
