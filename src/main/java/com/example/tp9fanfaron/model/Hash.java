package com.example.tp9fanfaron.model;

public class Hash {

    ///// Attributes

    private String id;
    private String key;

    ///// Constructor

    public Hash(String id, String key) {
        this.id = id;
        this.key = key;
    }

    ///// Functions

    ///// Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
