package com.example.cedar.database2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cedar on 2017/12/14.
 */

public class Membase extends SQLiteOpenHelper {
    private static final String DB_name = "mem.db";
    private static final int DB_version = 1;
    private static final String TABLE_name = "member";
    private static final String SQL_CREATE_TABLE =
            "create table if not exists "
            + TABLE_name
            + "(id integer, name text primary key,"
            + " birth text, info text);";

    public Membase(Context context){
        super(context, DB_name, null, DB_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    public long insert(Member entity){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id", entity.getID());
        values.put("name", entity.getName());
        values.put("birth", entity.getBirth());
        values.put("info", entity.getInfo());

        long rid = db.insert(TABLE_name, null, values);
        db.close();
        return rid;
    }

    public int deleteByName(String name){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name=?";
        String[] whereArgs = { name };
        int row = db.delete(TABLE_name, whereClause, whereArgs);
        db.close();
        return row;
    }

    public int update(Member entity){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name=?";
        String[] whereArgs = { entity.getName() };
        ContentValues values = new ContentValues();
        values.put("id", entity.getID());
        values.put("name", entity.getName());
        values.put("birth", entity.getBirth());
        values.put("info", entity.getInfo());
        int rows = db.update(TABLE_name, values, whereClause, whereArgs);
        db.close();
        return rows;
    }

    public Member getByName(String name){
        Member m = null;
        SQLiteDatabase db = getReadableDatabase();
        String selection = "name=?";
        String[] selectionArgs = { name };
        Cursor c = db.query(TABLE_name, null, selection, selectionArgs,
                null, null, null);
        if (c.moveToNext()){
            int id = c.getInt(0);
            String Name = c.getString(1);
            String birth = c.getString(2);
            String present = c.getString(3);
            m = new Member(id, Name, birth, present);
        }
        c.close();
        db.close();
        return m;
    }

    public Cursor getCursor(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_name, null, null,
                null,null,null,null);
        return cursor;
    }

    public int getSize(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_name, null, null, null,
                null, null, null);
        return c.getCount();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

