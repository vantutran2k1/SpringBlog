package com.tutran.springblog.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
@SQLDelete(sql = "UPDATE comments SET deleted = true WHERE id=?")
@SQLRestriction(value = "deleted = false")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ToString.Include
    @EqualsAndHashCode.Include
    private long id;

    @Column(name = "name", nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = "description", nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String description;

    @Column(name = "deleted", nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;
}
