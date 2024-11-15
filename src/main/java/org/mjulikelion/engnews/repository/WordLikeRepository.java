package org.mjulikelion.engnews.repository;

import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.entity.WordLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WordLikeRepository extends JpaRepository<WordLike, UUID> {
    Optional<WordLike> findByUserIdAndId(UUID userId, UUID id);
    List<WordLike> findAllByUser(User user);
}
