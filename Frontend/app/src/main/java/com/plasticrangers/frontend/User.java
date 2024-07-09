package com.plasticrangers.frontend;

// Define a Java object to represent the data
public class User {
    private String name;
    private String email;
    private String photoUrl;
    private int points;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String photoUrl, int points) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getPoints() {
        return points;
    }
}

