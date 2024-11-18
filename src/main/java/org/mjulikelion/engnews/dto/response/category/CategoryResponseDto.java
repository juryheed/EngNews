package org.mjulikelion.engnews.dto.response.category;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.Category;

import java.util.UUID;

@Getter
@Builder
public class CategoryResponseDto {
    private UUID id;
    private String category;

    public static CategoryResponseDto from(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .category(category.getCategory())
                .build();
    }
}
