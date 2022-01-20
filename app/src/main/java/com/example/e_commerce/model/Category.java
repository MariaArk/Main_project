package com.example.e_commerce.model;

public class Category {
    // у каждой категории должен быть свой поорядковый номер и название
    int id;
    String title;
    // через конструктор мы сразу сможем устанавливать идентификатор и название
    public Category(int id, String title) {
        this.id = id;
        this.title = title;
    }
    // создаем геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
