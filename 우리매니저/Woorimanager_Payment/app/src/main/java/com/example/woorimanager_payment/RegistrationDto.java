package com.example.woorimanager_payment;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class RegistrationDto {
    @SerializedName("reg_id")
    private String token;
    @SerializedName("phone")
    private String phone;

    public RegistrationDto() { }

    public RegistrationDto(String token) {
        this.token = token;
    }

    public RegistrationDto(String token, String phone) {
        this.token = token;
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NonNull
    @Override
    public String toString() {
        return "token : " + token + "\nphone : " + phone;
    }
}