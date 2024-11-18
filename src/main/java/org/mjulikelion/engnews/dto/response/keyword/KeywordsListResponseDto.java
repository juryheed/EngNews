package org.mjulikelion.engnews.dto.response.keyword;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.Keyword;

import java.util.List;

@Getter
@Builder
public class KeywordsListResponseDto {
    private List<KeywordResponseDto> keywords;

    public static KeywordsListResponseDto from(List<Keyword> keywords){
        return KeywordsListResponseDto.builder()
                .keywords(keywords.stream()
                        .map(KeywordResponseDto::from)
                        .toList())
                .build();
    }
}