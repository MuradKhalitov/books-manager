package ru.skillbox.BooksManager.exception;

public class BooksNotFoundException extends RuntimeException{
    public BooksNotFoundException(String message) {
        super(message);
    }
}
