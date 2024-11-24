package org.mjulikelion.engnews.dto.ai;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackDto {
    private String gpt_answer;

    public static FeedbackDto from(String feedBack){
        return FeedbackDto.builder()
                .gpt_answer(feedBack)
                .build();
    }
}
