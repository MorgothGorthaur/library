package com.example.library.service;

import com.example.library.dto.BookDto;
import com.example.library.exception.AuthorContainsBookException;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.model.Actuality;
import com.example.library.model.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService{
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private ModelMapper mapper;
    @Override
    public void checkIfAnotherAuthorsHaveThisBook(Book b) {
        var books = bookRepository.findAll().stream().filter(book -> book.getAuthors().size() != 0).toList();
        for (var book : books){
            if(book.equals(b)){
                b.setId(book.getId());
            }
        }
    }

}
