package org.mjulikelion.engnews.repository;

import org.mjulikelion.engnews.entity.CategoryOptions;
import org.mjulikelion.engnews.entity.KeywordOptions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface KeywordOptionsRepository extends JpaRepository<KeywordOptions, UUID> {
    List<KeywordOptions> findAllByCategoryOptions(CategoryOptions categoryOptions);
}
