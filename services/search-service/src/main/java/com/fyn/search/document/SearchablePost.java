package com.fyn.search.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(indexName = "posts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchablePost {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;

    private String authorId;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;
}
