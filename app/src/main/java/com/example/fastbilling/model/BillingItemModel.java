package com.example.fastbilling.model;

import android.content.Context;

public class BillingItemModel {
    String title,description,price,imei,imageName,imageUrl;

    public BillingItemModel(String title, String description, String price, String imei, String imageName, String imageUrl) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imei = imei;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
