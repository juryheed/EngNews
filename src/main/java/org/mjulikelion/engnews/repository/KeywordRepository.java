package org.mjulikelion.engnews.repository;

import org.mjulikelion.engnews.entity.Category;
import org.mjulikelion.engnews.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KeywordRepository extends JpaRepository<Keyword, UUID> {
    List<Keyword> findAllByCategory(Category category);
    Optional<Keyword> findById(UUID id);
    List<Keyword> findAllByCategoryIn(List<Category> categories);
}
