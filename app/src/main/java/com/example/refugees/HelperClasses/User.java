package com.example.refugees.HelperClasses;

public class User {
    private String user_id, image_url, full_name, email, phone_no, password;
    private Address address;



    public User(String userId, String image_url, String full_name, String email, String phone_no, String password, Address address) {
        this.user_id = userId;
        this.image_url = image_url;
        this.full_name = full_name;
        this.email = email;
        this.phone_no = phone_no;
        this.password = password;
        this.address = address;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}