package org.mjulikelion.engnews.dto.ai.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class E2kResponseDto {
    private String answer;

    public static E2kResponseDto from(String feedBack){
        return E2kResponseDto.builder()
                .answer(feedBack)
                .build();
    }
}
