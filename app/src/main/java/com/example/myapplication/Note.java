package com.example.myapplication;

public class Note {
    private int id;
    private String date;
    private String description;

    public Note() {}

    public Note(String date, String description) {
        this.date = date;
        this.description = description;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}