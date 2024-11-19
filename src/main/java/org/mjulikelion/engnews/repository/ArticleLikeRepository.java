package org.mjulikelion.engnews.repository;

import org.mjulikelion.engnews.entity.ArticleLike;
import org.mjulikelion.engnews.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, UUID> {
    List<ArticleLike> findAllByUser(User user);
    Optional<ArticleLike> findByUserAndId(User user, UUID articleLikeId);
}
