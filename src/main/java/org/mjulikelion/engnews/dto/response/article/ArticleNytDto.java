package org.mjulikelion.engnews.dto.response.article;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ArticleNytDto {
    private String title;
    private String imageUrl;
    private String content;
    private String time;
    private String journalistName;
    private Boolean isArticleLike;

    // 정적 팩토리 메서드로 객체 생성
    public static ArticleNytDto from(String title, String imageUrl, String content, String time, String journalistName, Boolean isArticleLike) {
        return ArticleNytDto.builder()
                .title(title)
                .imageUrl(imageUrl)
                .content(content)
                .time(time)
                .journalistName(journalistName)
                .isArticleLike(isArticleLike)
                .build();
    }
}
