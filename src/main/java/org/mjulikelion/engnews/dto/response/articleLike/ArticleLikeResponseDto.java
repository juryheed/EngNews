package org.mjulikelion.engnews.dto.response.articleLike;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.ArticleLike;

import java.util.UUID;

@Getter
@Builder
public class ArticleLikeResponseDto {
    private UUID id;
    private String originalUrl;
    private String title;
    private String imageUrl;
    private String content;

    public static ArticleLikeResponseDto from(ArticleLike articleLike, String title, String imageUrl, String content) {
        return ArticleLikeResponseDto.builder()
                .id(articleLike.getId())
                .originalUrl(articleLike.getOriginalUrl())
                .title(title)
                .imageUrl(imageUrl)
                .content(content)
                .build();
    }

    public static ArticleLikeResponseDto from(ArticleLike articleLike, String title, String content) {
        return ArticleLikeResponseDto.builder()
                .id(articleLike.getId())
                .originalUrl(articleLike.getOriginalUrl())
                .title(title)
                .imageUrl(null)
                .content(content)
                .build();
    }
}
