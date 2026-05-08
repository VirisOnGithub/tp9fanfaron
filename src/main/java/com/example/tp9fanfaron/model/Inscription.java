package com.example.tp9fanfaron.model;

public class Inscription {
    private String name;
    private String instrument;
    private String status;

    public Inscription(String name, String instrument, String status) {
        this.name = name;
        this.instrument = instrument;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getInstrument() {
        return instrument;
    }

    public String getStatus() {
        return status;
    }
}
