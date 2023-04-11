package com.example.fastbilling.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Items {

    @SerializedName("Title")
    @Expose
    private String Title;

    @SerializedName("Desciption")
    @Expose
    private String Desciption;

    @SerializedName("Price")
    @Expose
    private String Price;

    @SerializedName("IMEI")
    @Expose
    private String IMEI;

    //-----------------------------------

    public String getDesciption() {
        return Desciption;
    }

    public void setDesciption(String desciption) {
        Desciption = desciption;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
