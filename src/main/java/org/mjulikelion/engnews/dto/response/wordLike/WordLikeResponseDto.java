package org.mjulikelion.engnews.dto.response.wordLike;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.WordLike;

import java.util.UUID;

@Getter
@Builder
public class WordLikeResponseDto {
    private UUID id;
    private String word;
    private String translate;

    public static WordLikeResponseDto from(WordLike wordLike) {
        return WordLikeResponseDto.builder()
                .id(wordLike.getId())
                .word(wordLike.getWord())
                .translate(wordLike.getTranslate())
                .build();
    }
}
