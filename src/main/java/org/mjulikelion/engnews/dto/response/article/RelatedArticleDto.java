package org.mjulikelion.engnews.dto.response.article;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RelatedArticleDto {
    private String title;
    private String link;
    private String imageUrl;

    public static RelatedArticleDto from(String title, String link, String imageUrl) {
        return RelatedArticleDto.builder()
                .title(title)
                .link(link)
                .imageUrl(imageUrl)
                .build();
    }
}
