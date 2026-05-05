package com.example.tp9fanfaron.model;

import java.time.LocalDateTime;

public class Event {

    ///// Attributes

    private String id;
    private String type;
    private String name;
    private LocalDateTime dateTime;
    private Integer lengthInMinutes;
    private String place;
    private String description;

    ///// Constructor

    public Event(String id, String type, String name, LocalDateTime dateTime, Integer lengthInMinutes, String place, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.dateTime = dateTime;
        this.lengthInMinutes = lengthInMinutes;
        this.place = place;
        this.description = description;
    }

    ///// Functions

    ///// Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getLengthInMinutes() {
        return lengthInMinutes;
    }

    public void setLengthInMinutes(Integer lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
