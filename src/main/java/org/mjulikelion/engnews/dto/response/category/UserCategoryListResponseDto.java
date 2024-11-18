package org.mjulikelion.engnews.dto.response.category;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.Category;

import java.util.List;

@Getter
@Builder
public class UserCategoryListResponseDto {
    private List<CategoryResponseDto> categories;

    public static UserCategoryListResponseDto from(List<Category> categories){
        return UserCategoryListResponseDto.builder()
                .categories(categories.stream()
                        .map(CategoryResponseDto::from)
                        .toList())
                .build();
    }
}
