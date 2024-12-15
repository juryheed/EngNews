package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mjulikelion.engnews.dto.request.category.CategoryListDto;
import org.mjulikelion.engnews.dto.response.category.CategoryInfoDto;
import org.mjulikelion.engnews.dto.response.category.CategoryListResponseDto;
import org.mjulikelion.engnews.dto.response.category.CategoryResponseDto;
import org.mjulikelion.engnews.dto.response.category.UserCategoryListResponseDto;
import org.mjulikelion.engnews.entity.Category;
import org.mjulikelion.engnews.entity.CategoryOptions;
import org.mjulikelion.engnews.entity.KeywordOptions;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.entity.type.CategoryType;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.NotFoundException;
import org.mjulikelion.engnews.repository.CategoryOptionsRepository;
import org.mjulikelion.engnews.repository.CategoryRepository;
import org.mjulikelion.engnews.repository.KeywordOptionsRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryOptionsRepository categoryOptionsRepository;
    private final KeywordOptionsRepository keywordOptionsRepository;

    public CategoryListResponseDto getNaverCategories() {
        return getCategoriesByNews("naver");
    }

    public CategoryListResponseDto getNytCategories() {
        return getCategoriesByNews("nyt");
    }

    public CategoryListResponseDto getCategoriesByNews(String news) {
        List<CategoryResponseDto> categoryList = Arrays.stream(CategoryType.values())
                .map(categoryType -> {
                    CategoryOptions categoryOptions = categoryOptionsRepository.findByCategoryType(categoryType)
                            .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

                    if (!news.equalsIgnoreCase(categoryOptions.getNews())) {
                        return null;
                    }

                    return CategoryResponseDto.builder()
                            .id(categoryOptions.getId())
                            .category(categoryType)
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return CategoryListResponseDto.from(categoryList);
    }

    public void saveCategories(User user, CategoryListDto categoryDto) {
        CategoryType categoryType = isValidCategory(categoryDto.getCategory());

        CategoryOptions categoryOptions = categoryOptionsRepository.findByCategoryType(categoryType)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        Category newCategory = Category.builder()
                .category(categoryType)
                .user(user)
                .categoryOptions(categoryOptions)
                .build();

        categoryRepository.save(newCategory);
    }

    public List<CategoryInfoDto> getCategoryInfo(User user) {
        List<Category> categories = categoryRepository.findAllByUser(user);
        return categories.stream()
                .map(category -> {
                    List<KeywordOptions> keywordOptions = keywordOptionsRepository.findAllByCategoryOptions(category.getCategoryOptions());
                    return CategoryInfoDto.from(category, keywordOptions);
                })
                .collect(Collectors.toList());
    }

    private CategoryType isValidCategory(String category) {
        boolean exist = Arrays.stream(CategoryType.values())
                .anyMatch(c -> c.name().equals(category));
        if (!exist){
            throw new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        return CategoryType.valueOf(category);
    }

    public UserCategoryListResponseDto getAllUserCategories(User user) {
        List<Category> categories = categoryRepository.findAllByUser(user);
        return UserCategoryListResponseDto.from(categories);
    }

    public void deleteCategory(User user, UUID categoryId) {
        Category category = categoryRepository.findByUserAndId(user, categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }
}
