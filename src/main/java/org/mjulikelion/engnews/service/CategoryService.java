package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mjulikelion.engnews.dto.request.category.CategoryListDto;
import org.mjulikelion.engnews.dto.response.category.CategoryListResponseDto;
import org.mjulikelion.engnews.dto.response.category.UserCategoryListResponseDto;
import org.mjulikelion.engnews.entity.Category;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.entity.type.CategoryType;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.NotFoundException;
import org.mjulikelion.engnews.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryListResponseDto getAllCategories() {
        List<String> categories = Arrays.stream(CategoryType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return CategoryListResponseDto.from(categories);
    }

    public void saveCategories(User user, CategoryListDto categories) {
        for (String category : categories.getCategories()) {
            CategoryType categoryType = isValidCategory(category);
            Category newCategory = Category.builder()
                    .category(categoryType)
                    .user(user)
                    .build();
            categoryRepository.save(newCategory);
        }
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
