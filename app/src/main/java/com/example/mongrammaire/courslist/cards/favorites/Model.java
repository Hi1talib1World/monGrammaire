package com.example.mongrammaire.courslist.cards.favorites;

public class Model {

    private String Title,Description,category,content;
    private int img ,heart, progress, id;

    public Model() {
        super();
    }
    public Model(int id, String Title, String Description, int img, int heart) {
        super();
        this.id = id;
        this.Title = Title;
        this.Description = Description;
        this.img = img;
        this.heart = heart;
    }

    public Model(int id, String Title, String Description, int img, int heart, String category) {
        this(id, Title, Description, img, heart);
        this.category = category;
    }

    public Model(int id, String Title, String Description, int img, int heart, String category, int progress) {
        this(id, Title, Description, img, heart, category);
        this.progress = progress;
    }

    public Model(int id, String title, String description, String content, int img, String category, int progress) {
        this(id, title, description, img, 0, category, progress);
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getheart() {
        return heart;
    }

    public void setheart(int img) {
        this.heart = heart;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }


}
