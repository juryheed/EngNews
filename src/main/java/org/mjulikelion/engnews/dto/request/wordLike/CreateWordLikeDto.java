package org.mjulikelion.engnews.dto.request.wordLike;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateWordLikeDto {
    @NotBlank(message = "찜할 단어를 입력해주세요.")
    private String word;
    @NotBlank(message = "찜할 단어의 번역을 입력해주세요.")
    private String translate;
}
