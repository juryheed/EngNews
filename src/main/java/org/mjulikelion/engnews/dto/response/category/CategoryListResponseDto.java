package org.mjulikelion.engnews.dto.response.category;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryListResponseDto {
    private List<CategoryResponseDto> categories;

    public static CategoryListResponseDto from(List<CategoryResponseDto> categories){
        return CategoryListResponseDto.builder()
                .categories(categories)
                .build();
    }
}
