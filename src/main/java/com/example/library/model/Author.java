package com.example.library.model;


import com.example.library.enums.Role;
import com.example.library.exception.AuthorContainsBookException;
import com.example.library.exception.AuthorDoesntContainsBookException;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors", uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "name", "second_name"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "second_name")
    private String secondName;
    private int age;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Book> books;

    @Column(name = "role")
    private Role role;

    private String password;
    private String email;

    public void addBook(Book book) {
        if (books != null) {
            if (books.contains(book)) {
                throw new AuthorContainsBookException();
            }
            books.add(book);
        } else {
            books = new HashSet<>();
            books.add(book);
        }
    }

    public void removeBook(Book book) {
        if (books != null && books.contains(book)) {
            books.remove(book);
        } else {
            throw new AuthorDoesntContainsBookException();
        }
    }
}
