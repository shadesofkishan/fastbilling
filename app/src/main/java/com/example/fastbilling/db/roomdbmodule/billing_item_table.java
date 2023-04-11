package com.example.fastbilling.db.roomdbmodule;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "billing_items_table")
public class billing_item_table {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("title")
    @Expose
    private String Title;

    @SerializedName("discription")
    @Expose
    private String Desciption;

    @SerializedName("price")
    @Expose
    private String Price;

    @SerializedName("imei")
    @Expose
    private String IMEI;

    //-----------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


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
