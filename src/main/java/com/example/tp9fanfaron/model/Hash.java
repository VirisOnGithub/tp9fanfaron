package com.example.tp9fanfaron.model;

public class Hash {

    ///// Attributes

    private Integer id;
    private String key;

    ///// Constructor

    public Hash(Integer id, String key) {
        this.id = id;
        this.key = key;
    }

    ///// Functions

    ///// Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
