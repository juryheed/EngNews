package org.mjulikelion.engnews.dto.response.keyword;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.KeywordOptions;

import java.util.UUID;

@Getter
@Builder
public class KeywordOptionDto {
    private UUID keywordOptionId;
    private String keywordName;

    public static KeywordOptionDto from(KeywordOptions keywordOptions) {
        return KeywordOptionDto.builder()
                .keywordOptionId(keywordOptions.getId())
                .keywordName(keywordOptions.getKeywordName())
                .build();
    }
}
