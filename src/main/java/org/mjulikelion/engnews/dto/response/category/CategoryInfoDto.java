package org.mjulikelion.engnews.dto.response.category;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.dto.response.keyword.KeywordOptionDto;
import org.mjulikelion.engnews.entity.Category;
import org.mjulikelion.engnews.entity.KeywordOptions;
import org.mjulikelion.engnews.entity.type.CategoryType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class CategoryInfoDto {
    private UUID categoryId;
    private CategoryType category;
    private List<KeywordOptionDto> keywordOptions;

    public static CategoryInfoDto from(Category category, List<KeywordOptions> keywords) {
        List<KeywordOptionDto> keywordOptionDtos = keywords.stream()
                .map(KeywordOptionDto::from)
                .collect(Collectors.toList());

        return CategoryInfoDto.builder()
                .categoryId(category.getId())
                .category(category.getCategory())
                .keywordOptions(keywordOptionDtos)
                .build();
    }
}
