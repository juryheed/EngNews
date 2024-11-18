package org.mjulikelion.engnews.dto.request.category;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListDto {
    @NotEmpty(message = "카테고리를 입력해주세요.")
    private List<String> categories;
}
