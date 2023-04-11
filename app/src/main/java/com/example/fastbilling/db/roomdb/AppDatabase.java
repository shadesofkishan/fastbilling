package com.example.fastbilling.db.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fastbilling.db.roomdbmodule.Item_list;
import com.example.fastbilling.model.ItemsModel;


@Database(entities = {Item_list.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
