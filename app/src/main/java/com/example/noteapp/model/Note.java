package com.example.noteapp.model;

public class Note {
    public String id;
    public String title;
    public String des;
    public String img;
    public String file;
    public String time;

    public Note() {
    }

    public Note(String id, String title, String des, String img, String file, String time) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.img = img;
        this.file = file;
        this.time = time;
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
