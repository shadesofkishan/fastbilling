package com.example.fastbilling.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DATABASENAME = "billinglist";

    public DBHandler(@Nullable Context context) {
        super(context, DATABASENAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "create table item_list(id integer primary key autoincrement, title text, discription text, price text, imei text, imagename, imagepath)";
        sqLiteDatabase.execSQL(query);

        String query2 = "create table billing_items_table(id integer primary key autoincrement, title text, discription text, price text, imei text, imagename, imagepath)";
        sqLiteDatabase.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS item_list";
        sqLiteDatabase.execSQL(query);

        String query2 = "DROP TABLE IF EXISTS billing_items_table";
        sqLiteDatabase.execSQL(query2);
        onCreate(sqLiteDatabase);

    }

    public String additems(String title, String discription, String price, String imei,String imgname, String imgpath)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title",title);
        cv.put("discription",discription);
        cv.put("price",price);
        cv.put("imei",imei);
        cv.put("imagename",imgname);
        cv.put("imagepath",imgpath);
        float res = db.insert("item_list",null,cv);

        if (res == -1)
        {
            return "Failed";
        }
        else
        {
            return "Successfully inserted";
        }

    }

    public String addBillingitems(String title, String discription, String price, String imei, String imgName, String imgPath)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title",title);
        cv.put("discription",discription);
        cv.put("price",price);
        cv.put("imei",imei);
        float res = db.insert("billing_items_table",null,cv);

        if (res == -1)
        {
            return "Failed";
        }
        else
        {
            return "Successfully inserted";
        }

    }

    public Cursor readAllData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query = "select * from item_list order by id desc";
        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }

    public Cursor getData(String gImei) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from "+ "item_list" +" where "+"imei"+ " =?";
        Cursor res =  db.rawQuery(query, new String[]{gImei});
        return res;
    }

     public Cursor deleteItem(String gImei){

         SQLiteDatabase db = this.getReadableDatabase();
         String query = "delete from "+ "item_list" +" where "+"imei"+ " =?";
         Cursor res =  db.rawQuery(query, new String[]{gImei});
         return res;
     }

    public Cursor deleteBillingItem(String gImei){

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "delete from "+ "billing_items_table" +" where "+"imei"+ " =?";
        Cursor res =  db.rawQuery(query, new String[]{gImei});
        return res;
    }


}
