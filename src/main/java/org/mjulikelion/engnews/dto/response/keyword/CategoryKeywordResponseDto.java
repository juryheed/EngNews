package org.mjulikelion.engnews.dto.response.keyword;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.Keyword;
import org.mjulikelion.engnews.entity.type.CategoryType;

import java.util.UUID;


//카테고리별 키워드 조회시 전달되는 데이터
@Getter
@Builder
public class CategoryKeywordResponseDto {
    private CategoryType categoryOption;
    private UUID userCategoryId;
    private String keyword;
    private UUID keywordId;

    public static  CategoryKeywordResponseDto from(Keyword keyword){
        return CategoryKeywordResponseDto.builder()
                .categoryOption(keyword.getCategory().getCategoryOptions().getCategoryType())    //카테고리옵션 아이디
                .userCategoryId(keyword.getCategory().getId())  //유저가 설정한 키워드
                .keyword(keyword.getKeyword())  //키워드 이름
                .keywordId(keyword.getId())    //키워드 아이디
                .build();
    }
}
