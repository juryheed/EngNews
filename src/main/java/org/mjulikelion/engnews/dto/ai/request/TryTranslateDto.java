package org.mjulikelion.engnews.dto.ai.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TryTranslateDto {
    String news_sentence;
    String message;
}
