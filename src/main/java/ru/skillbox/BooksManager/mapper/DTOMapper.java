package ru.skillbox.BooksManager.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.BooksManager.dto.BookDTO;
import ru.skillbox.BooksManager.dto.CategoryDTO;
import ru.skillbox.BooksManager.model.Book;
import ru.skillbox.BooksManager.model.Category;
import ru.skillbox.BooksManager.repository.CategoryRepository;

@Component
public class DTOMapper {
    private final CategoryRepository categoryRepository;

    @Autowired
    public DTOMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public BookDTO convertToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setCategoryName(book.getCategory().getName());
        return bookDTO;
    }

    public Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        Category category = categoryRepository.findByName(bookDTO.getCategoryName());
        if (category == null){
            category = new Category();
            category.setName(bookDTO.getCategoryName());
            categoryRepository.save(category);
        }
        book.setCategory(category);
        return book;
    }

    public CategoryDTO convertToDTO (Category category){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }
    public Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return category;
    }
}
