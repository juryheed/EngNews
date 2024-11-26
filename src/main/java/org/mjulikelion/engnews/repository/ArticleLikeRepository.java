package org.mjulikelion.engnews.repository;

import org.mjulikelion.engnews.entity.ArticleLike;
import org.mjulikelion.engnews.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, UUID> {
    Optional<ArticleLike> findByUserAndId(User user, UUID articleLikeId);
    List<ArticleLike> findAllByUserAndNews(User user, String news);
    List<ArticleLike> findAllByUser(User user);
}
