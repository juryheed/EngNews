package org.mjulikelion.engnews.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "keyword_options")
public class KeywordOptions extends BaseEntity {
    @Column(nullable = false)
    private String keywordName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_options_id", nullable = false)
    private CategoryOptions categoryOptions;

    @Column(nullable = false)
    private String news;
}
