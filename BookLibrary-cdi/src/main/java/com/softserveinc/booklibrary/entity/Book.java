package com.softserveinc.booklibrary.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "year_published", nullable = false)
    private Integer yearPublished;

    @Column(name = "isbn", nullable = false, unique = true)
    private Integer isbn;

    @Column(name = "publisher")
    private String publisher;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ManyToMany(mappedBy = "books")
    private Set<Author> authors;

    @OneToMany(mappedBy = "book")
    private Set<Review> reviews;
}
