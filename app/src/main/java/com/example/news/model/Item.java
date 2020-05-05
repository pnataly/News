package com.example.news.model;

import android.graphics.Bitmap;

import java.util.Date;

public class Item implements Comparable<Item> {

    private String title;
    private String description;
    private String link;
    private String time;
    private Bitmap image;

    public Item() { }

    public Item(String title, String description, String link, String time) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.time = time;
    }

    public Item(String title, String description, String link, String time, Bitmap image) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.time = time;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public int compareTo(Item o) {
        Date d1 = new Date(getTime());
        Date d2 = new Date(o.getTime());
        return d1.compareTo(d2);
    }
}
