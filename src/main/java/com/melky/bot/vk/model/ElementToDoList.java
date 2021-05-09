package com.melky.bot.vk.model;

import javax.persistence.*;
import java.util.Comparator;

@Entity
public class ElementToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String text;
    boolean isSuccess;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ElementToDoList() {
    }

    public ElementToDoList(String text, User user) {
        this.text = text;
        this.user = user;
        isSuccess = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

