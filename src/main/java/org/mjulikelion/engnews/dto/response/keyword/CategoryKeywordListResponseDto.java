package org.mjulikelion.engnews.dto.response.keyword;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.Keyword;

import java.util.List;

@Getter
@Builder
public class CategoryKeywordListResponseDto {
    private List<CategoryKeywordResponseDto> keywords;

    public static CategoryKeywordListResponseDto from(List<Keyword> keywords){
        return CategoryKeywordListResponseDto.builder()
                .keywords(keywords.stream()
                        .map(CategoryKeywordResponseDto::from)
                        .toList())
                .build();
    }
}
