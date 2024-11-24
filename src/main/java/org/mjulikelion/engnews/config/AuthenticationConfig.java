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
                .addPathPatterns("/users/**","/words-like/**","/categories/**","/keywords/**","/news/**", "/articles-like/**", "/news/nyt/keyword")
                .excludePathPatterns("/categories/naver", "/categories/nyt", "/news/naver/**", "/news/nyt/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserArgumentResolver);
    }
}
