package org.mjulikelion.engnews.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    //카테고리
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Category> categories;

    //찜한 뉴스
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ArticleLike> articleLikes;

    //찜한 단어
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private  List<WordLike> wordLikes;

}
