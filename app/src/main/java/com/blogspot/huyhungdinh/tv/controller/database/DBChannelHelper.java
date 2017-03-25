package com.blogspot.huyhungdinh.tv.controller.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by HUNGDH on 5/8/2016.
 */
public class DBChannelHelper extends SQLiteOpenHelper {

    Context context;
    String DB_PATH = "";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "tivi.sqlite";

    public DBChannelHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        DB_PATH = context.getFilesDir().getParent() + "/databases/" + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase openDatabase() {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void copyDatabase() {
        try {
            InputStream is = context.getAssets().open(DB_NAME);
            OutputStream os = new FileOutputStream(DB_PATH);
            int length;
            byte[] buffer = new byte[1024];
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("myLog", "Error Copy Database " + e.toString());
        }
    }

    public void createDatabase() {
        boolean kt = checkDB();
        if (kt) {
            Log.d("myLog", "Ket noi thanh cong!");
        } else {
            Log.d("myLog", "Ket noi that bai!");
            this.getWritableDatabase();
            copyDatabase();
        }
    }

    public boolean checkDB() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }
}
