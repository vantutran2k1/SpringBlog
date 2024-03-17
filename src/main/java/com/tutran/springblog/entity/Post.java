package com.tutran.springblog.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
@SQLDelete(sql = "UPDATE posts SET deleted = true WHERE id=?")
@SQLRestriction(value = "deleted = false")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    private long id;

    @Column(name = "title", nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String title;

    @Column(name = "description", nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String description;

    @Column(name = "content", nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String content;

    @Column(name = "deleted", nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private boolean deleted = Boolean.FALSE;
}
