package com.example.noteapp.model;

public class News {
    public String id;
    public String title;
    public String content;
    public String img;
    public boolean dexuat;
    public String createAt;

    public News() {
    }

    public News(String id, String title, String content, String img, boolean dexuat, String createAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.img = img;
        this.dexuat = dexuat;
        this.createAt = createAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isDexuat() {
        return dexuat;
    }

    public void setDexuat(boolean dexuat) {
        this.dexuat = dexuat;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
