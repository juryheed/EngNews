package org.mjulikelion.engnews.dto.response.user;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.dto.response.category.CategoryResponseDto;
import org.mjulikelion.engnews.dto.response.keyword.KeywordResponseDto;
import org.mjulikelion.engnews.entity.Category;
import org.mjulikelion.engnews.entity.Keyword;
import org.mjulikelion.engnews.entity.User;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class MypageDto {
    private UUID id;
    private String name;
    private String email;

    private List<CategoryResponseDto> categories;
    private List<KeywordResponseDto> keywords;

    public static MypageDto from(User user, List<Keyword> keywords, List<Category> categories) {
        return MypageDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .categories(categories.stream()
                        .map(CategoryResponseDto::from)
                        .toList())
                .keywords(keywords.stream()
                        .map(KeywordResponseDto::from)
                        .toList())
                .build();
    }

}
