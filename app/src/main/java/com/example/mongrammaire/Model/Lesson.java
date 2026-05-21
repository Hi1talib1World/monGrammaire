package com.example.mongrammaire.Model;

public class Lesson {
    private int id;
    private String title;
    private String description;
    private int level;
    private boolean isLocked;

    public Lesson(int id, String title, String description, int level) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.level = level;
        this.isLocked = true;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getLevel() { return level; }
    public boolean isLocked() { return isLocked; }
    public void setLocked(boolean locked) { isLocked = locked; }
}
