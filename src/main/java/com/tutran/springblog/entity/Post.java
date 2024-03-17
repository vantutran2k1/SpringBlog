package com.tutran.springblog.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
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
}
