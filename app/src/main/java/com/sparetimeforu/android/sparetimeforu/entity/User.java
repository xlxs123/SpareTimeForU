package com.sparetimeforu.android.sparetimeforu.entity;

import java.io.Serializable;

/**
 * SpareTimeForU
 * Created by Jin on 2018/11/4.
 */

public class User implements Serializable {

    private String nickname;
    private String signature;
    private String avatar_url;
    private String favourable_rate;
    private String phone;
    private String gender;
    private String email;
    private String auth_token;
    private int user_id;
    private float money;

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getBg_url() {
        return bg_url;
    }

    public void setBg_url(String bg_url) {
        this.bg_url = bg_url;
    }

    private String bg_url;

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getFavourable_rate() {
        return favourable_rate;
    }


    public void setFavourable_rate(String favourable_rate) {
        this.favourable_rate = favourable_rate;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", signature='" + signature + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", favourable_rate='" + favourable_rate + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", auth_token='" + auth_token + '\'' +
                ", user_id=" + user_id +
                ", money=" + money +
                ", bg_url='" + bg_url + '\'' +
                '}';
    }
}
