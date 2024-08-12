package com.Server.saleWebsite.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public BookDTO saveBook(BookDTO bookDTO) {
        Book book = BookMapper.mapToBook(bookDTO);
        book = bookRepository.save(book);
        return BookMapper.mapToBookDto(book);
    }

    public List<BookDTO> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookMapper::mapToBookDto)
                .collect(Collectors.toList());
    }

    public BookDTO findBookById(Long id) {
        return bookRepository.findById(id)
                .map(BookMapper::mapToBookDto)
                .orElse(null); // Hoặc throw exception nếu cần
    }

    public BookDTO updateBook(BookDTO bookDTO) {
        Book book = BookMapper.mapToBook(bookDTO);
        book = bookRepository.save(book);
        return BookMapper.mapToBookDto(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
