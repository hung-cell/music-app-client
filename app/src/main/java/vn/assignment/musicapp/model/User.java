package vn.assignment.musicapp.model;

import java.io.Serializable;

public class User implements Serializable {
    String email;
    String password;
    String image;
    String uid;
    public User() {
    }

    public User(String email, String password, String image) {
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public User(String email, String uid){
        this.email = email;
        this.uid = uid;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
