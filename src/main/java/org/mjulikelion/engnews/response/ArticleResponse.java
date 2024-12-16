package org.mjulikelion.engnews.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleResponse {
    private Response response;

    @Getter
    @Setter
    public static class Response {
        private List<Doc> docs;

        @Getter
        @Setter
        public static class Doc {
            private String web_url; // 기사 URL
            private String snippet; // 요약된 내용
            private String pub_date; // 발행 날짜
            private Headline headline; // 기사 제목
            private Byline byline; // 기자 정보
            private List<Multimedia> multimedia; // 멀티미디어 정보

            @Getter
            @Setter
            public static class Headline {
                private String main; // 메인 제목
            }

            @Getter
            @Setter
            public static class Byline {
                private String original; // 기자 이름
            }

            @Getter
            @Setter
            public static class Multimedia {
                private String url; // 이미지 URL
            }
        }
    }
}
