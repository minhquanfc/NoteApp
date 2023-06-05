package com.example.noteapp.model;

public class Users {
    public String uId;
    public String name;
    public String email;
    public String passWord;
    public String role;
    public boolean active;
    public String avt;

    public Users() {
    }

    public Users(String uId, String name, String email, String passWord, String role, boolean active, String avt) {
        this.uId = uId;
        this.name = name;
        this.email = email;
        this.passWord = passWord;
        this.role = role;
        this.active = active;
        this.avt = avt;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }
}
