package org.mjulikelion.engnews.dto.request.articleLike;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ArticleLikeUrlDto {
    @NotEmpty(message = "저장할 기사의 url을 입력해주세요.")
    private String originalUrl;
    @NotEmpty(message = "저장할 기사의 사이트를 입력해주세요.")
    private String news;
}
