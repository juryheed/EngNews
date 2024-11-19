package org.mjulikelion.engnews.dto.response.articleLike;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ArticleLikeListResponseDto {
    private List<ArticleLikeResponseDto> articleLikes;

    public static ArticleLikeListResponseDto from(List<ArticleLikeResponseDto> articleLikeDtos) {
        return ArticleLikeListResponseDto.builder()
                .articleLikes(articleLikeDtos)
                .build();
    }
}
