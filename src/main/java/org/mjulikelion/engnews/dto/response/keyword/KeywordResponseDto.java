package org.mjulikelion.engnews.dto.response.keyword;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.Keyword;
import org.mjulikelion.engnews.entity.type.CategoryType;

import java.util.UUID;

@Getter
@Builder
public class KeywordResponseDto {
    private UUID id;
    private String keyword;

    public static  KeywordResponseDto from(Keyword keyword){
        return KeywordResponseDto.builder()
                .id(keyword.getId())
                .keyword(keyword.getKeyword())
                .build();
    }
}