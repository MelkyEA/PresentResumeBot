package com.melky.bot.vk.model;

import com.melky.bot.vk.VKBotState;

import javax.persistence.*;
import java.util.*;


@Entity
public class User {
    @Id
    private Long userId;
    private VKBotState vkBotState;
    private String city;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ElementToDoList> elementToDoListSet;
    @Transient
    private ArrayList<ElementToDoList> toDoLists;
    private int editElement;

    public User() {
    }

    public User(Long userId, VKBotState vkBotState) {
        this.userId = userId;
        this.vkBotState = vkBotState;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public VKBotState getVkBotState() {
        return vkBotState;
    }

    public void setVkBotState(VKBotState vkBotState) {
        this.vkBotState = vkBotState;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<ElementToDoList> getElementToDoListSet() {
        return elementToDoListSet;
    }

    public void setElementToDoListSet(Set<ElementToDoList> elementToDoListSet) {
        this.elementToDoListSet = elementToDoListSet;
    }

    public ArrayList<ElementToDoList> getToDoLists() {
        toDoLists = new ArrayList<>();
        TreeMap<Long, ElementToDoList> mapOfList = new TreeMap<>();
        if(getElementToDoListSet() != null && !getElementToDoListSet().isEmpty()) {
            for (ElementToDoList element : getElementToDoListSet()) {
                mapOfList.put(element.getId(), element);
            }
            for(Map.Entry<Long, ElementToDoList> entry : mapOfList.entrySet()) {
                toDoLists.add(entry.getValue());
            }
        }
        return toDoLists;
    }

    public void setToDoLists(ArrayList<ElementToDoList> toDoLists) {
        this.toDoLists = toDoLists;
    }

    public int getEditElement() {
        return editElement;
    }

    public void setEditElement(int editElement) {
        this.editElement = editElement;
    }
}
