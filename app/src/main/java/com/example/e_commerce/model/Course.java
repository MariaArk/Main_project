package com.example.e_commerce.model;

public class Course {
    // инициализируем все нужные поля
    int id, category;
    String img;
    String title;
    String price;
    String level;
    String color;
    String text;
    // через конструктор мы сразу сможем устанавливать идентификатор и название, цену, уровень сложности, описание, цвет и категорию
    public Course(int id, String img, String title, String price, String level, String color, String text, int category) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.price = price;
        this.level = level;
        this.color = color;
        this.text = text;
        this.category = category;
    }
    // создаем геттеры и сеттеры
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
