package com.example.mongrammaire.courslist.cards;

public class Model {

    private String Title,Description;
    private int img ,heart;

    public Model() {
        super();
    }
    public Model(String Title, String Description, int img, int heart) {
        super();
        this.Title = Title;
        this.Description = Description;
        this.img = img;
        this.heart = heart;
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
