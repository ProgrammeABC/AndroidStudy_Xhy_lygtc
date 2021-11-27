package cn.edu.lygtc.hello;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyAppDBHelper extends SQLiteOpenHelper {
    public MyAppDBHelper(Context context){
        super(context, "studyApp.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(_id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(20), password VARCHAR(20))");
        db.execSQL("CREATE TABLE book(_id INTEGER PRIMARY KEY AUTOINCREMENT, time VARCHAR, total VARCHAR, description VARCHAR, category VARCHAR(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}