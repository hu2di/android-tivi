package com.blogspot.huyhungdinh.tv.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blogspot.huyhungdinh.tv.model.Channel;
import com.blogspot.huyhungdinh.tv.model.Schedule;

import java.util.ArrayList;

/**
 * Created by HUNGDH on 5/8/2016.
 */
public class DBChannelController {

    SQLiteDatabase db;
    DBChannelHelper helper;

    public static final String TABLE_CHANNEL = "channel";
    public static final String TABLE_CATALOG = "catalog";
    public static final String TABLE_SCHEDULE = "schedule";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String LINK = "link";
    public static final String IMAGE = "image";
    public static final String CATALOG = "catalog";
    public static final String FAVORITE = "favorite";
    public static final String DESCRIPTION = "description";

    public static final String CHANNEL = "channel";
    public static final String CONTENT = "content";
    public static final String TIME = "time";

    public DBChannelController(Context context) {
        helper = new DBChannelHelper(context);
        helper.createDatabase();
        db = helper.openDatabase();
    }

    public Channel getChannel(int id) {
        Channel channel;
        String query = "SELECT * FROM " + TABLE_CHANNEL + " WHERE " + ID + " = " + id;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (!c.isAfterLast()) {
            String name = c.getString(1);
            String link = c.getString(2);
            String image = c.getString(3);
            int catalog = c.getInt(4);
            int favorite = c.getInt(5);
            String description = c.getString(6);
            channel = new Channel(id, name, link, image, catalog, favorite, description);
            return channel;
        } else {
            return null;
        }
    }

    public int Update(Channel channel) {
        ContentValues cv = new ContentValues();
        if (channel.isFavorite()) {
            cv.put(FAVORITE, 1);
        } else {
            cv.put(FAVORITE, 0);
        }
        int result = db.update(TABLE_CHANNEL, cv, ID + "=?", new String[]{"" + channel.getId()});
        return result;
    }

    public int UpdateLink(int id, String link) {
        ContentValues cv = new ContentValues();
        cv.put(LINK, link);
        int result = db.update(TABLE_CHANNEL, cv, ID + "=?", new String[]{"" + id});
        return result;
    }

    public ArrayList<Channel> getAll() {
        ArrayList<Channel> list = new ArrayList<Channel>();
        String query = "SELECT * FROM " + TABLE_CHANNEL;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String link = c.getString(2);
            String image = c.getString(3);
            int catalog = c.getInt(4);
            int favorite = c.getInt(5);
            String description = c.getString(6);
            Channel item = new Channel(id, name, link, image, catalog, favorite, description);
            list.add(item);
            c.moveToNext();
        }
        return list;
    }

    public ArrayList<Channel> getFavorite() {
        ArrayList<Channel> list = new ArrayList<Channel>();
        String query = "SELECT * FROM " + TABLE_CHANNEL + " WHERE " + FAVORITE + " = 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String link = c.getString(2);
            String image = c.getString(3);
            int catalog = c.getInt(4);
            int favorite = c.getInt(5);
            String description = c.getString(6);
            Channel item = new Channel(id, name, link, image, catalog, favorite, description);
            list.add(item);
            c.moveToNext();
        }
        return list;
    }

    public ArrayList<Channel> getCatalog(int id_cat) {
        ArrayList<Channel> list = new ArrayList<Channel>();
        String query = "SELECT * FROM " + TABLE_CHANNEL + " WHERE " + CATALOG + " = " + id_cat;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String link = c.getString(2);
            String image = c.getString(3);
            int catalog = c.getInt(4);
            int favorite = c.getInt(5);
            String description = c.getString(6);
            Channel item = new Channel(id, name, link, image, catalog, favorite, description);
            list.add(item);
            c.moveToNext();
        }
        return list;
    }

    public ArrayList<Channel> search(String key) {
        ArrayList<Channel> list = new ArrayList<Channel>();
        String query = "SELECT * FROM " + TABLE_CHANNEL + " WHERE " + NAME + " LIKE '%" + key + "%'";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String link = c.getString(2);
            String image = c.getString(3);
            int catalog = c.getInt(4);
            int favorite = c.getInt(5);
            String description = c.getString(6);
            Channel item = new Channel(id, name, link, image, catalog, favorite, description);
            list.add(item);
            c.moveToNext();
        }
        return list;
    }

    public ArrayList<Schedule> getAllSchedule() {
        ArrayList<Schedule> list = new ArrayList<Schedule>();
        String query = "SELECT * FROM " + TABLE_SCHEDULE;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            int id = c.getInt(0);
            int idChannel = c.getInt(1);
            String content = c.getString(2);
            String time = c.getString(3);
            Schedule item = new Schedule(id, idChannel, content, time);
            list.add(item);
            c.moveToNext();
        }
        return list;
    }

    public long addSchedule(Schedule schedule) {
        ContentValues cv = new ContentValues();
        cv.put(ID, schedule.getId());
        cv.put(CHANNEL, schedule.getIdChannel());
        cv.put(CONTENT, schedule.getContent());
        cv.put(TIME, schedule.getTime());
        long id = db.insert(TABLE_SCHEDULE, null, cv);
        return id;
    }

    public long deleteSchedule(Schedule schedule) {
        long id = db.delete(TABLE_SCHEDULE, ID + "=?", new String[]{"" + schedule.getId()});
        return id;
    }
}
