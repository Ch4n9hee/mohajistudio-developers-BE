package com.mohajistudio.developers.database.repository.post;

import com.mohajistudio.developers.database.dto.PostDetailsDto;
import com.mohajistudio.developers.database.dto.PostDto;
import com.mohajistudio.developers.database.dto.TagDto;
import com.mohajistudio.developers.database.dto.UserDto;
import com.mohajistudio.developers.database.enums.PostStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.UUID;

import static com.mohajistudio.developers.database.entity.QPost.post;
import static com.mohajistudio.developers.database.entity.QPostTag.postTag;
import static com.mohajistudio.developers.database.entity.QTag.tag;
import static com.mohajistudio.developers.database.entity.QUser.user;
import static com.querydsl.core.group.GroupBy.*;


@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * PageableExecutionUtils.getPage() 을 사용하여 페이징 결과를 반환합니다.
     * result, pageable, totalCount.fetch().size() 등을 전달하여 page 인터페이스르 구현하는 객체를 반환합니다.
     * 이 방식은 실제 페이지 데이터와 총 데이터 수가 필요할 때만 총 개수를 조회하여 성능을 최적화합니다.
     * 예를 들어, 전체 데이터가 10인데, size가 10이라면 totalCount 쿼리는 실행되지 않습니다.
     * 위 기능을 사용하기 위해서 JPAQuery를 이용하며,
     * 해당 클래스는 쿼리 조각을 미리 조립하고 totalCount.fetch().size() 함수가 호출될 때 실제 쿼리가 수행됩니다.
     */
    @Override
    public Page<PostDto> findAllPostDto(Pageable pageable, PostStatus status) {
        List<PostDto> posts = jpaQueryFactory.select(post, tag, user)
                .from(post)
                .join(user).on(post.userId.eq(user.id))
                .leftJoin(postTag).on(post.id.eq(postTag.postId))
                .leftJoin(tag).on(postTag.tagId.eq(tag.id))
                .where(eqStatus(status))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(post.id)
                                .list(Projections
                                        .constructor(
                                                PostDto.class,
                                                post.id,
                                                Projections.constructor(UserDto.class,
                                                        user.id,
                                                        user.nickname,
                                                        user.email,
                                                        user.password,
                                                        user.role
                                                ),
                                                post.title,
                                                post.summary,
                                                post.thumbnail,
                                                post.status,
                                                post.publishedAt,
                                                set(Projections.constructor(TagDto.class,
                                                                tag.id,
                                                                tag.title,
                                                                tag.slug,
                                                                tag.description
                                                        ).skipNulls()
                                                )
                                        )
                                )
                );

        JPAQuery<Long> totalCount = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(eqStatus(PostStatus.PUBLISHED));

        return PageableExecutionUtils.getPage(posts, pageable, () -> totalCount.fetch().size());
    }

    @Override
    public PostDetailsDto findByIdPostDetailsDto(UUID id) {
        List<PostDetailsDto> posts = jpaQueryFactory.select(post, tag, user)
                .from(post)
                .join(user).on(post.userId.eq(user.id))
                .leftJoin(postTag).on(post.id.eq(postTag.postId))
                .leftJoin(tag).on(postTag.tagId.eq(tag.id))
                .where(eqId(id))
                .transform(
                        groupBy(post.id)
                                .list(Projections
                                        .constructor(
                                                PostDetailsDto.class,
                                                post.id,
                                                Projections.constructor(UserDto.class,
                                                        user.id,
                                                        user.nickname,
                                                        user.email,
                                                        user.password,
                                                        user.role
                                                ),
                                                post.title,
                                                post.content,
                                                post.summary,
                                                post.thumbnail,
                                                post.status,
                                                post.publishedAt,
                                                set(Projections.constructor(TagDto.class,
                                                        tag.id,
                                                        tag.title,
                                                        tag.slug,
                                                        tag.description
                                                ).skipNulls())
                                        )
                                )
                );

        if(posts.isEmpty()) return null;

        return posts.get(0);
    }

    private BooleanExpression eqStatus(PostStatus status) {
        if (status == null) return null;
        return post.status.eq(status);
    }

    private BooleanExpression eqId(UUID id) {
        if (id == null) return null;
        return post.id.eq(id);
    }
}
