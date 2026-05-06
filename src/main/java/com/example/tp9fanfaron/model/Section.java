package com.example.tp9fanfaron.model;

public class Section {

    ///// Attributes

    private Integer id;
    private String name;

    ///// Constructor

    public Section(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    ///// Functions

    ///// Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
