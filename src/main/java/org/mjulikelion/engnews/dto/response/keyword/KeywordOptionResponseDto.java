package org.mjulikelion.engnews.dto.response.keyword;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.KeywordOptions;
import org.mjulikelion.engnews.entity.type.CategoryType;

import java.util.UUID;

@Getter
@Builder
public class KeywordOptionResponseDto {
    private UUID keywordId;
    private String keywordName;
    private UUID categoryId;
    private CategoryType categoryName;

    public static KeywordOptionResponseDto from(KeywordOptions keywordOptions){
        return KeywordOptionResponseDto.builder()
                .keywordId(keywordOptions.getId())
                .keywordName(keywordOptions.getKeywordName())
                .categoryId(keywordOptions.getCategoryOptions().getId())
                .categoryName(keywordOptions.getCategoryOptions().getCategoryType())
                .build();
    }
}
