package com.tanvis.hostel;

import jakarta.persistence.*;

@Entity
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String title;
    String message;
    String posted_on;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getPosted_on() { return posted_on; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setMessage(String message) { this.message = message; }
    public void setPosted_on(String posted_on) { this.posted_on = posted_on; }
}