package org.mjulikelion.engnews.dto.response.article;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.dto.request.article.CategoryRequestDto;

@Getter
@Builder
public class CategoryArticleDto {
    private String title;
    private String link;
    private String imageUrl;

    public static CategoryArticleDto from(String title, String link, String imageUrl) {
        return CategoryArticleDto.builder()
                .title(title)
                .link(link)
                .imageUrl(imageUrl)
                .build();
    }
}
