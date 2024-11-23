package org.mjulikelion.engnews.dto.request.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListDto {
    @NotNull(message = "카테고리 아이디를 입력해주세요.")
    private UUID categoryId;
    @NotEmpty(message = "카테고리를 이름을 입력해주세요.")
    private String category;
}
