package com.sparetimeforu.android.sparetimeforu.entity;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by Jin on 2018/11/4.
 */

public class Errand extends LitePalSupport implements Serializable {
    private String user_Email;
    private String user_Avatar;
    private String user_Nickname;
    private String content;
    private String picture_url_1;
    private String picture_url_2;
    private String picture_url_3;
    private String origin;
    private String destination;
    private int like_number;
    private int comment_number;
    private int id;
    private int is_deleted;
    private int is_finished;
    private String release_time;
    private String end_time;
    private float money;
    private int evaluate;
    private int receiver_id;
    private String receiver_email;
    private String receiver_nickname;
    private int is_received;
    private int errand_id;

    public String getReceiver_email() {
        return receiver_email;
    }

    public void setReceiver_email(String receiver_email) {
        this.receiver_email = receiver_email;
    }

    public String getReceiver_nickname() {
        return receiver_nickname;
    }

    public void setReceiver_nickname(String receiver_nickname) {
        this.receiver_nickname = receiver_nickname;
    }

    public int getIs_confirmed_finish() {
        return is_confirmed_finish;
    }

    public void setIs_confirmed_finish(int is_confirmed_finish) {
        this.is_confirmed_finish = is_confirmed_finish;
    }

    private int is_confirmed_finish;

    public int getIs_finished() {
        return is_finished;
    }

    public void setIs_finished(int is_finished) {
        this.is_finished = is_finished;
    }

    public String getUser_Email() {
        return user_Email;
    }

    public void setUser_Email(String user_Email) {
        this.user_Email = user_Email;
    }

    public String getUser_Avatar() {
        return user_Avatar;
    }

    public void setUser_Avatar(String user_Avatar) {
        this.user_Avatar = user_Avatar;
    }

    public String getUser_Nickname() {
        return user_Nickname;
    }

    public void setUser_Nickname(String user_Nickname) {
        this.user_Nickname = user_Nickname;
    }

    public int getErrand_id() {
        return errand_id;
    }

    public void setErrand_id(int errand_id) {
        this.errand_id = errand_id;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture_url_1() {
        return picture_url_1;
    }

    public void setPicture_url_1(String picture_url_1) {
        this.picture_url_1 = picture_url_1;
    }

    public String getPicture_url_2() {
        return picture_url_2;
    }

    public void setPicture_url_2(String picture_url_2) {
        this.picture_url_2 = picture_url_2;
    }

    public String getPicture_url_3() {
        return picture_url_3;
    }

    public void setPicture_url_3(String picture_url_3) {
        this.picture_url_3 = picture_url_3;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getLike_number() {
        return like_number;
    }

    public void setLike_number(int like_number) {
        this.like_number = like_number;
    }

    public int getComment_number() {
        return comment_number;
    }

    public void setComment_number(int comment_number) {
        this.comment_number = comment_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(int evaluate) {
        this.evaluate = evaluate;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public int getIs_received() {
        return is_received;
    }

    public void setIs_received(int is_received) {
        this.is_received = is_received;
    }

    @Override
    public String toString() {
        return "content:" + this.content;
    }
}
