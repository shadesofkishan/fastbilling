package com.example.fastbilling.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private final static String IS_PICK_N_DEL = "IsPikndel";//
    private final static String USER_ID = "USER_ID";
    private final static String USER_PASSWORD = "USER_PASSWORD";


    private int PRIVATE_MODE = 0;
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this.context = context;
        this.preferences = this.context.getSharedPreferences("FASTBILLING", PRIVATE_MODE);
        this.editor = this.preferences.edit();
        this.editor.apply();
    }

    public void clearPreferences() {
        preferences.edit().clear().apply();
    }

    public void setIsPickNDel(int val){
        editor.putInt(IS_PICK_N_DEL,val);
        editor.commit();
    }

    public int getIsPickNDel() {
        return preferences.getInt(IS_PICK_N_DEL,0);
    }

    public String getUserId() {
        return preferences.getString(USER_ID,"");
    }

    public void setUserId(String userId){
        editor.putString(USER_ID,userId);
        editor.commit();
    }

    public String getUserPassword() {
        return preferences.getString(USER_PASSWORD,"");
    }

    public void setUserPassword(String password){
        editor.putString(USER_PASSWORD,password);
        editor.commit();
    }


}
