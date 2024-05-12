package ru.skillbox.BooksManager.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.skillbox.BooksManager.dto.BookDTO;
import ru.skillbox.BooksManager.mapper.DTOMapper;
import ru.skillbox.BooksManager.model.Book;
import ru.skillbox.BooksManager.model.Category;
import ru.skillbox.BooksManager.repository.BookRepository;
import ru.skillbox.BooksManager.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DTOMapper dtoMapper;

    public BookService() {
    }

    @Cacheable(value = "booksByTitleAndAuthor", key = "#title + #author")
    public List<BookDTO> findByTitleAndAuthor(String title, String author) {
        List<Book> books = bookRepository.findByTitleAndAuthor(title, author);
        List<BookDTO> bookDTOList = books.stream()
                .map(dtoMapper::convertToDTO)
                .collect(Collectors.toList());
        log.info("поиск по названию {} и по автору {}", title, author);
        return bookDTOList;
    }

    @Cacheable(value = "booksByCategory", key = "#categoryName")
    public List<BookDTO> findByCategory(String categoryName) {
        List<Book> books = bookRepository.findByCategoryName(categoryName);
        List<BookDTO> bookDTOList = books.stream()
                .map(dtoMapper::convertToDTO)
                .collect(Collectors.toList());
        log.info("поиск по категории {}", categoryName);
        return bookDTOList;
    }

    @CacheEvict(value = {"booksByTitleAndAuthor", "booksByCategory"}, allEntries = true)
    public BookDTO createBook(BookDTO bookDTO) {
        Category category = categoryRepository.findByName(bookDTO.getCategoryName());
        if (category == null) {
            category = new Category();
            category.setName(bookDTO.getCategoryName());
            category = categoryRepository.save(category);
        }
        // Создаем книгу
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setCategory(category);
        bookRepository.save(book);
        BookDTO bookDTO1 = dtoMapper.convertToDTO(book);
        log.info("Создание книги title: {}, author: (), category: {}",
                bookDTO1.getTitle(), bookDTO1.getAuthor(), bookDTO1.getCategoryName());
        return bookDTO1;
    }

    @CacheEvict(value = {"booksByTitleAndAuthor", "booksByCategory"}, allEntries = true)
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Category category = categoryRepository.findByName(bookDTO.getCategoryName());
        if (category == null) {
            category = new Category();
            category.setName(bookDTO.getCategoryName());
            category = categoryRepository.save(category);
        }
        // Проверяем существует ли книга
        Book existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook == null) {
            return null;
        }
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setCategory(category);
        bookRepository.save(existingBook);
        BookDTO bookDTO1 = dtoMapper.convertToDTO(existingBook);
        log.info("Изменили книгу id: {}", id);
        return bookDTO1;
    }

    @CacheEvict(value = {"booksByTitleAndAuthor", "booksByCategory"}, allEntries = true)
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
        log.info("Удалили книгу id: {}", id);
    }
}

