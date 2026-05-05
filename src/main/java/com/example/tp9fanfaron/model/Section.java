package com.example.tp9fanfaron.model;

public class Section {

    ///// Attributes

    private String id;
    private String name;

    ///// Constructor

    public Section(String id, String name) {
        this.id = id;
        this.name = name;
    }

    ///// Functions

    ///// Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
