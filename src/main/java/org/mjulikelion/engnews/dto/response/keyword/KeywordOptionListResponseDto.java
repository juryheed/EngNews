package org.mjulikelion.engnews.dto.response.keyword;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.KeywordOptions;

import java.util.List;

@Getter
@Builder
public class KeywordOptionListResponseDto {
    private List<KeywordOptionResponseDto> keywords;

    public static KeywordOptionListResponseDto from(List<KeywordOptions> keywordOptions){
        return KeywordOptionListResponseDto.builder()
                .keywords(keywordOptions.stream()
                        .map(KeywordOptionResponseDto::from)
                        .toList())
                .build();
    }
}
