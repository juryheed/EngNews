package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mjulikelion.engnews.dto.request.category.CategoryListDto;
import org.mjulikelion.engnews.dto.response.category.NaverCategoryListResponseDto;
import org.mjulikelion.engnews.dto.response.category.UserCategoryListResponseDto;
import org.mjulikelion.engnews.entity.Category;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.NotFoundException;
import org.mjulikelion.engnews.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public NaverCategoryListResponseDto getAllNaverCategories() throws IOException {
        String newsURL = "https://news.naver.com";
        Document doc = Jsoup.connect(newsURL).get();

        Elements elements = doc.select("ul.Nlnb_menu_list > li.Nlist_item > a.Nitem_link > span.Nitem_link_menu");

        List<String> categories = new ArrayList<>();
        for (Element element : elements) {
            categories.add(element.text());
        }

        return NaverCategoryListResponseDto.from(categories);
    }

    public void saveCategories(User user, CategoryListDto categories) {
        for (String category : categories.getCategories()) {
            Category newCategory = Category.builder()
                    .category(category)
                    .user(user)
                    .build();
            categoryRepository.save(newCategory);
        }
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
