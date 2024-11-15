package org.mjulikelion.engnews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.WordLike;

import java.util.Comparator;
import java.util.List;

@Getter
@Builder
public class WordLikeListResponseData {
    private List<WordLikeResponseDto> wordLikeList;

    public static WordLikeListResponseData from(List<WordLike> wordLikes){
        return WordLikeListResponseData.builder()
                .wordLikeList(wordLikes.stream()
                        .sorted(Comparator.comparing(WordLike::getCreatedAt).reversed())
                        .map(WordLikeResponseDto::from)
                        .toList())
                .build();
    }
}
