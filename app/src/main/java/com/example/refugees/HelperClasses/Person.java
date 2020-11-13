package com.example.refugees.HelperClasses;

public class Person {

    String image_url, full_name, user_id;

    public Person() {
    }

    public Person(String image_url, String full_name) {
        this.image_url = image_url;
        this.full_name = full_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
    
