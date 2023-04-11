package com.example.fastbilling.db.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fastbilling.db.roomdbmodule.Item_list;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM item_list")
    List<Item_list> getAll();

    @Query("SELECT * FROM item_list where Title=:title")
    List<Item_list> getItemByTitle(String title);

    @Insert
    void insertAll(Item_list... items);

    @Delete
    void delete(Item_list item);
}
