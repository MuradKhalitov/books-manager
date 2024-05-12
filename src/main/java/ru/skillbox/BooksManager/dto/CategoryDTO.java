package ru.skillbox.BooksManager.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CategoryDTO implements Serializable{
    private Long id;
    private String name;
}

