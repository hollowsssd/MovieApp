package com.example.movieapp.Models;

public class User {
    public String uid;
    public String email;
    public String displayName;
    public boolean isAdmin;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public User() {} // Bắt buộc cho Firestore

        public User(String uid, String email, String displayName, boolean isAdmin) {
            this.uid = uid;
            this.email = email;
            this.displayName = displayName;
            this.isAdmin = isAdmin;
        }
}
