package com.tutran.springblog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
@SQLDelete(sql = "UPDATE comments SET deleted = true WHERE id=?")
@SQLRestriction(value = "deleted = false")
public class Comment {
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

    @Column(name = "email", nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "body", nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String body;

    @Column(name = "deleted", nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private boolean deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
