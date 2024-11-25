package org.mjulikelion.engnews.dto.request.keyword;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeywordDto {

    @NotNull(message = "관련 카테고리를 입력해 주세요")
    private UUID categoryId;

    @NotNull(message = "키워드를 입력해주세요.")
    private UUID keywordId;
}
