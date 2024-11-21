package org.mjulikelion.engnews.dto.response.article;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ArticleDto {
    private String title;
    private String imageUrl;
    private String content;

    public static ArticleDto from(String title, String imageUrl, String content) {
        return ArticleDto.builder()
                .title(title)
                .imageUrl(imageUrl)
                .content(content)
                .build();
    }
}
