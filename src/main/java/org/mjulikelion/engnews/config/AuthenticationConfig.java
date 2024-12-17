package org.mjulikelion.engnews.config;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUserArgumentResolver;
import org.mjulikelion.engnews.authentication.AuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthenticatedUserArgumentResolver authenticatedUserArgumentResolver;
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns(
                        "/users/**","/words-like/**","/categories/**","/keywords/**","/articles-like/**",
                        "/news/**","/news/nyt/keyword","/news/naver/keyword",
                        "/translate","/news/naver","/news/nyt","/translate_t5_e2k","/translate_t5_k2e", "/analyze-sentence",
                        "/try-translate/sentence","/try-translate/message",
                        "/try-summarize/content","/try-summarize/message")

                .excludePathPatterns(
                        "/categories/naver", "/categories/nyt",
                        "/news/naver/categories","/news/naver/top5", "/news/naver/related-articles",
                        "/keywords/{categoryId}", "/keywords/naver", "/keywords/nyt",
                        "/news/nyt/keyword","/news/nyt/categories","/news/nyt/top5");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserArgumentResolver);
    }
}
