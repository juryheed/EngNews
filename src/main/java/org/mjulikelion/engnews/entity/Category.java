package org.mjulikelion.engnews.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String category;

    //유저와의 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;


    //키워드와의 관계
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<keyword> keywords;

}