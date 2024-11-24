package org.mjulikelion.engnews.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mjulikelion.engnews.entity.type.CategoryType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category_options")
public class CategoryOptions extends BaseEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Column(nullable = false)
    private String news;
}
