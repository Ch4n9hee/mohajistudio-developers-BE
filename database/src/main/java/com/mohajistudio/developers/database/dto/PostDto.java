package com.mohajistudio.developers.database.dto;

import com.mohajistudio.developers.database.enums.PostStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    private UUID id;
    private UserDto user;
    private String title;
    private String summary;
    private String thumbnail;
    private PostStatus status;
    private LocalDateTime publishedAt;
    private Set<TagDto> tags;

    @QueryProjection
    public PostDto(UUID id, UserDto user, String title, String summary, String thumbnail, PostStatus status, LocalDateTime publishedAt, Set<TagDto> tags) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.summary = summary;
        this.thumbnail = thumbnail;
        this.status = status;
        this.publishedAt = publishedAt;
        this.tags = tags;
    }
}
