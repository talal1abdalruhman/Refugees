package com.example.refugees.HelperClasses;

public class Address {
    private String governorate, city;

    public Address() {
    }

    public Address(String governorate, String city) {
        this.governorate = governorate;
        this.city = city;
    }

    public String getGovernorate() {
        return governorate;
    }

    public void setGovernorate(String governorate) {
        this.governorate = governorate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return  governorate + " / " + city;
    }
}