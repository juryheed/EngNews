package org.mjulikelion.engnews.dto.response.keyword;

import lombok.Builder;
import lombok.Getter;
import org.mjulikelion.engnews.entity.Keyword;
import org.mjulikelion.engnews.entity.type.CategoryType;

import java.util.UUID;

@Getter
@Builder
public class KeywordResponseDto {
    private CategoryType categoryOption;
    private UUID userCategoryId;
    private String keyword;
    private UUID keywordId;


    public static  KeywordResponseDto from(Keyword keyword){
        return KeywordResponseDto.builder()
                .categoryOption(keyword.getCategory().getCategoryOptions().getCategoryType())    //카테고리옵션 아이디
                .userCategoryId(keyword.getCategory().getId())  //유저가 설정한 키워드
                .keyword(keyword.getKeyword())  //키워드 이름
                .keywordId(keyword.getId())    //키워드 아이디
                .build();
    }
}