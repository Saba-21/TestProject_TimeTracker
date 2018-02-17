package com.example.pc.testproject_timetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 17-Feb-18.abc
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBModel.TABLE_NAME + " (" +
                    DBModel.Task + " TEXT PRIMARY KEY," +
                    DBModel.Description + " TEXT," +
                    DBModel.Time + " TEXT)";


    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBModel.TABLE_NAME;

    private static final int DB_VERSION = 1;

    DBHelper(Context context) { super(context, "MyDb.db", null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(SQL_CREATE_ENTRIES); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    List<DataModel> getData(){

        List<DataModel> data = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(DBModel.TABLE_NAME,
                new String[]{DBModel.Task,
                        DBModel.Description,
                        DBModel.Time},
                null, null, null, null, null);

        cursor.moveToFirst();

        do {
            DataModel dataModel = new DataModel();

            if (cursor.getCount()>0) {
                dataModel.setTask(cursor.getString(cursor.getColumnIndex(DBModel.Task)));
                dataModel.setDescription(cursor.getString(cursor.getColumnIndex(DBModel.Description)));
                dataModel.setTime(cursor.getString(cursor.getColumnIndex(DBModel.Time)));
            }
            data.add(dataModel);

        } while (cursor.moveToNext());

        return data;
    }

    void addData(DataModel dataModel) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBModel.Task, dataModel.getTask());
        contentValues.put(DBModel.Description, dataModel.getDescription());
        contentValues.put(DBModel.Time, dataModel.getTime());

        db.insert(DBModel.TABLE_NAME, null, contentValues);
    }

    void dropData() {

        SQLiteDatabase db = getWritableDatabase();

        db.delete(DBModel.TABLE_NAME, "1", null);
    }

}
