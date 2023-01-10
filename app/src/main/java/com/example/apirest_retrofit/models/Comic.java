package com.example.apirest_retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comic {

    @SerializedName("img")
    @Expose
    private String img;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("day")
    @Expose
    private String day;

    public void setImg(String img) {
        this.img = img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getDay() {
        return day;
    }
}
