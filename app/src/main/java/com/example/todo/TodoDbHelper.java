package com.example.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDbHelper extends SQLiteOpenHelper {

    public static TodoDbHelper singlton;

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "sqlite.db";

    public TodoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static TodoDbHelper getInstance(Context context) {
        if (singlton == null) {
            singlton = new TodoDbHelper(context);
        }
        return singlton;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SUBJECT_TABLE);
        db.execSQL(SQL_CREATE_TASK_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_SUBJECT_TABLE);
        db.execSQL(SQL_DELETE_TASK_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String SQL_CREATE_SUBJECT_TABLE =
            "CREATE TABLE IF NOT EXISTS subject (" +
                    "   id INTEGER PRIMARY KEY," +
                    "   name TEXT NOT NULL " +
                    ");";

    private static final String SQL_CREATE_TASK_TABLE =
            "CREATE TABLE IF NOT EXISTS task (" +
                    "id INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "checked INTEGER NOT NULL, " +
                    "subject_id INTEGER NOT NULL, " +
                    "FOREIGN KEY (subject_id) " +
                    "      REFERENCES subject (id) " +
                    "         ON DELETE CASCADE " +
                    "         ON UPDATE NO ACTION " +
                    ");";

    private static final String SQL_DELETE_SUBJECT_TABLE =
            "DROP TABLE IF EXISTS subject;";

    private static final String SQL_DELETE_TASK_TABLE =
            "DROP TABLE IF EXISTS task;";
}