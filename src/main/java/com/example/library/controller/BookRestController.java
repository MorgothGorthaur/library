package com.example.library.controller;

import com.example.library.dto.BookDto;
import com.example.library.exception.AuthorContainsBookException;
import com.example.library.exception.AuthorDoesntContainsBookException;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.model.Actuality;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/library/books")
@AllArgsConstructor
public class BookRestController {
    private final ModelMapper modelMapper;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<BookDto> getAuthorBooks(Principal principal) {
        return bookRepository.getBooksByAuthorEmail(principal.getName()).stream().map(book -> modelMapper.map(book, BookDto.class)).toList();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public BookDto addBook(Principal principal, @RequestBody @Valid BookDto dto) throws AuthorContainsBookException {
        var author = authorRepository.findAuthorByEmailAndActuality(principal.getName(), Actuality.ACTIVE)
                .orElseThrow(() -> new AuthorNotFoundException(principal.getName()));
        author.addBook(dto.toBook());
        return modelMapper.map(authorRepository.save(author).getBooks().stream().filter(e -> e.equals(dto.toBook())).toList().get(0), BookDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping
    public BookDto changeBook(Principal principal, @RequestBody @Valid BookDto dto) {
        var books = bookRepository.checkIdAuthorContainsBooksWithSameTitle(principal.getName(), dto.getId(), dto.getTitle());
        if (books.size() > 1) {
            throw new AuthorContainsBookException();
        }
        var changed = bookRepository.checkIfAuthorContainsBookById(dto.getId(), principal.getName())
                .orElseThrow(AuthorDoesntContainsBookException::new);
        changed.setTitle(dto.getTitle());
        changed.setDescription(dto.getDescription());
        return modelMapper.map(bookRepository.save(changed), BookDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public String deleteBook(Principal principal, @PathVariable Long id) {
        var author = authorRepository.findAuthorByEmailAndActuality(principal.getName(), Actuality.ACTIVE).orElseThrow(() -> new AuthorNotFoundException(principal.getName()));
        var book = bookRepository.checkIfAuthorContainsBookById(id, principal.getName()).orElseThrow(() -> new BookNotFoundException(id));
        author.removeBook(book);
        authorRepository.save(author);
        return "deleted";
    }
}
