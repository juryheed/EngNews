package org.mjulikelion.engnews.dto.request.article;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CategoryRequestDto {
    @NotEmpty(message = "카테고리를 선택해주세요.")
    private String category;
}
