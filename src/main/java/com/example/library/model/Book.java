package com.example.library.model;

import lombok.*;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;


@Entity
@Table(name = "books")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	@ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
	List<Author> authors;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Book book = (Book) o;
		return title.equals(book.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title);
	}
}
