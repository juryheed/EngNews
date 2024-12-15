package org.mjulikelion.engnews.dto.ai.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class K2eResponseDto {
    private String answer;

    public static K2eResponseDto from(String feedBack){
        return K2eResponseDto.builder()
                .answer(feedBack)
                .build();
    }
}
