package com.example.imkonbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATA_BASE_NAME = "payment_system.db";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "user";
    private static final String COLUMN_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_TEL_NUMBER = "tel_number";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_TEL_PASSWORD = "tel_password";
    private static final String COLUMN_DEVICE_TOKEN = "device_token";



    public MyDataBaseHelper(@Nullable Context context) {
        super(context, DATA_BASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryUser = "CREATE TABLE "+TABLE_NAME+
                " ("+COLUMN_ID+" TEXT, "+
                COLUMN_USER_NAME+" TEXT, "+
                COLUMN_TEL_NUMBER+" TEXT, "+
                COLUMN_PASSWORD+" TEXT, "+
                COLUMN_TEL_PASSWORD+" TEXT, "+
                COLUMN_DEVICE_TOKEN+" TEXT )" ;
        db.execSQL(queryUser);
        String queryCards = "CREATE TABLE cards"+
                " (id TEXT, "+
                "user_id TEXT, "+
                "card_number TEXT, "+
                "live_time TEXT, "+
                "total_price TEXT )" ;
        db.execSQL(queryCards);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS cards");
        onCreate(db);
    }

    public boolean addUser(String id,String tel_number,String user_name, String password, String tel_password, String device_token){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID,id);
        cv.put(COLUMN_USER_NAME,user_name);
        cv.put(COLUMN_TEL_NUMBER,tel_number);
        cv.put(COLUMN_PASSWORD,password);
        cv.put(COLUMN_TEL_PASSWORD,tel_password);
        cv.put(COLUMN_DEVICE_TOKEN,device_token);

        long result = db.insert(TABLE_NAME,null,cv);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Cursor readAllData(){
        String query = "SELECT * FROM "+ TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =  db.rawQuery(query,null);

        return cursor;
    }


    public boolean addCard(String id,String user_id,String card_number, String live_time, String total_price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        cv.put("user_id",user_id);
        cv.put("card_number",card_number);
        cv.put("live_time",live_time);
        cv.put("total_price",total_price);

        long result = db.insert("cards",null,cv);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public boolean updateCard(String card_number, String total_price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("total_price",total_price);

        Cursor cursor = db.rawQuery("SELECT * FROM cards WHERE card_number=?", new String[]{card_number});
        long result = db.update("cards",cv,"card_number=?",new String[]{card_number});
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Cursor readCards(){
        String query = "SELECT * FROM cards";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =  db.rawQuery(query,null);

        return cursor;
    }

}

