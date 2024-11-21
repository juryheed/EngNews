package org.mjulikelion.engnews.dto.response.category;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryListResponseDto {
    private List<String> categories;

    public static CategoryListResponseDto from(List<String> categories){
        return CategoryListResponseDto.builder()
                .categories(categories)
                .build();
    }
}
