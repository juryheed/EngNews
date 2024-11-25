package org.mjulikelion.engnews.dto.response.article;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ArticleDto {
    private String title;
    private String imageUrl;
    private String content;
    private String time;
    private String journalistName;

    public static ArticleDto from(String title, String imageUrl, String content,String time, String journalistName) {
        return ArticleDto.builder()
                .title(title)
                .imageUrl(imageUrl)
                .content(content)
                .time(time)
                .journalistName(journalistName)
                .build();
    }
}
