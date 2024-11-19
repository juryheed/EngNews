package org.mjulikelion.engnews.dto.response.category;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NaverCategoryListResponseDto {
    private List<String> categories;

    public static NaverCategoryListResponseDto from(List<String> categories){
        return NaverCategoryListResponseDto.builder()
                .categories(categories)
                .build();
    }
}
