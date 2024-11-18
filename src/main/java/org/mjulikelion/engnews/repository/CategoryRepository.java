package org.mjulikelion.engnews.repository;

import org.mjulikelion.engnews.entity.Category;
import org.mjulikelion.engnews.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByUser(User user);
    Optional<Category> findByUserAndId(User user, UUID categoryId);
}
