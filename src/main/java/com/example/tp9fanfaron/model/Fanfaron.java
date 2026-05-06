package com.example.tp9fanfaron.model;

import java.time.LocalDateTime;

public class Fanfaron {

    ///// Attributes

    private Integer id;
    private String username;
    private String email;
    private String name;
    private String surname;
    private String gender;
    private String alimentaryConstraint;
    private LocalDateTime creationDate;
    private LocalDateTime lastConnectionDate;
    private Boolean isAdmin;
    private String password;

    ///// Constructor

    public Fanfaron(Integer id, String username, String email, String name, String surname, String gender, String alimentaryConstraint, LocalDateTime creationDate, LocalDateTime lastConnectionDate, Boolean isAdmin, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.alimentaryConstraint = alimentaryConstraint;
        this.creationDate = creationDate;
        this.lastConnectionDate = lastConnectionDate;
        this.isAdmin = isAdmin;
        this.password = password;
    }

    ///// Functions

    ///// Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAlimentaryConstraint() {
        return alimentaryConstraint;
    }

    public void setAlimentaryConstraint(String alimentaryConstraint) {
        this.alimentaryConstraint = alimentaryConstraint;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastConnectionDate() {
        return lastConnectionDate;
    }

    public void setLastConnectionDate(LocalDateTime lastConnectionDate) {
        this.lastConnectionDate = lastConnectionDate;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
