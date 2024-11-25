package org.mjulikelion.engnews.repository;

import org.mjulikelion.engnews.entity.CategoryOptions;
import org.mjulikelion.engnews.entity.type.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryOptionsRepository extends JpaRepository<CategoryOptions, UUID> {
    Optional<CategoryOptions> findByCategoryType(CategoryType categoryType);
    Optional<CategoryOptions> findById(UUID id);
}
