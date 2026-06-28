package com.example.mongrammaire.Model;

public class LessonModel {
    private int id;
    private String title;
    private String description;
    private String content;
    private String category;
    private int difficulty; // 1: Easy, 2: Medium, 3: Hard

    public LessonModel() {}

    public LessonModel(String title, String description, String content, String category, int difficulty) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.category = category;
        this.difficulty = difficulty;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
